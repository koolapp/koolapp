package test.koolapp.stream

import kotlin.test.*

import java.io.*
import java.nio.*
import java.nio.channels.*
import java.net.*
import org.fusesource.hawtdispatch.*
import org.junit.Test as test

/**
 * <p>
 * </p>
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
class EchoTest {

    test fun echoServer() {
        val server = EchoServer(0)
        try {
            println("Server started on port: "+ server.localPort())
            val socket = Socket("localhost", server.localPort())
            try {
                // write some data...
                socket.getOutputStream()!!.write("Hello World".toByteArray("UTF-8"))

                // it should get echoed back..
                val buffer = ByteArray(1024)
                var count = socket.getInputStream()!!.read(buffer)
                assertEquals("Hello World", String(buffer, 0, count, "UTF-8"))

            } finally {
                socket.close()
            }

        } finally {
            server.close()
        }
    }

}

fun main(args:Array<String>):Unit {
    EchoTest().echoServer()
}

class EchoServer(val port : Int) {

    val queue = Dispatch.createQueue()!!
    val channel = ServerSocketChannel.open()!!
    val acceptEvents = Dispatch.createSource(channel, SelectionKey.OP_ACCEPT, queue)!!;

    {
        channel.socket()!!.bind(InetSocketAddress(port))
        channel.configureBlocking(false)
        acceptEvents.setCancelHandler(runnable{
            println("Closed server socket on port ${localPort()}")
            channel.close()
        })
        acceptEvents.setEventHandler(runnable{

            // Keep accepting until we run out of sockets to open..
            while( true ) {
                val socket = channel.accept()
                if( socket != null ) {
                    try {
                        socket.configureBlocking(false)
                        EchoConnection(socket).resume()
                    } catch (e : Exception) {
                        socket.close()
                    }
                } else {
                    break;
                }
            }
        })
        acceptEvents.resume()
    }

    fun localPort() = channel.socket()!!.getLocalPort()

    fun close():Unit {
        acceptEvents.cancel()
    }
}

open class EchoConnection(val channel : SocketChannel) {

    val queue = Dispatch.createQueue()!!
    val buffer = ByteBuffer.allocate(1024)!!
    val readEvents = Dispatch.createSource(channel, SelectionKey.OP_READ, queue)!!
    val writeEvents = Dispatch.createSource(channel, SelectionKey.OP_WRITE, queue)!!

    ;
    {
        readEvents.setCancelHandler(runnable {
            writeEvents.cancel()
        })
        writeEvents.setCancelHandler(runnable{
            channel.close()
            println("Closed connection from: ${remoteAddress()}")
        })
        readEvents.setEventHandler(runnable{ processReads()  })
        writeEvents.setEventHandler(runnable{ processWrites() })
        println("Accepted connection from: ${remoteAddress()}")
    }

    fun resume() = readEvents.resume()
    fun suspend() = readEvents.suspend()
    fun remoteAddress() = channel.socket()?.getRemoteSocketAddress()?.toString()?:"n/a"
    fun close() = readEvents.cancel()

    var pendingRead :ByteArray? = null
    var lastReadOfferRejected = false

    fun processReads():Unit {
        queue.assertExecuting()
        try {
            while(true) {
                if( pendingRead !=null ) {
                    if( offerRead(pendingRead!!) ) {
                        pendingRead = null
                    } else {
                        // the pendingRead was not accepted.. so suspend from doing further reads.
                        if( !lastReadOfferRejected ) {
                            lastReadOfferRejected = true
                            readEvents.suspend();
                        }
                    }
                } else {
                    if (channel.read(buffer) == - 1) {
                        close()
                        break
                    } else {
                        buffer.flip()
                        if (buffer.remaining() > 0) {
                            println("Received: ${buffer.remaining()} bytes of data")
                            pendingRead = buffer.array()!!.copyOf(buffer.remaining())
                        }
                        buffer.clear()
                    }
                }
            }
        } catch(e : IOException) {
            close()
        }
    }

    open fun offerRead(data:ByteArray):Boolean = offerWrite(data)

    fun resumeReads() = queue.execute(runnable{
        if( lastReadOfferRejected ) {
            lastReadOfferRejected = false
            readEvents.resume();
            processReads();
        }
    })

    var pendingWrite :ByteBuffer? = null

    fun offerWrite(data:ByteArray):Boolean {
        queue.assertExecuting()
        if( pendingWrite==null ) {
            pendingWrite = ByteBuffer.wrap(data)
            processWrites()
            return true
        } else {
            return false
        }
    }

    fun processWrites():Unit {
        queue.assertExecuting()
        try {
            while( true ) {
                if (pendingWrite == null) {
                    if( !writeEvents.isSuspended() ) {
                        writeEvents.suspend()
                    }
                    return;
                } else {
                    if (pendingWrite?.remaining() == 0) {
                        pendingWrite = null
                    } else {
                        val count = channel.write(pendingWrite)
                        if( count == 0 ) {
                            if( writeEvents.isSuspended() ) {
                                writeEvents.resume()
                            }
                            return;
                        } else {
                            println("Sent: ${count} bytes of data")
                        }
                    }
                }
            }
        } catch(e : IOException) {
            close()
        }
    }

}