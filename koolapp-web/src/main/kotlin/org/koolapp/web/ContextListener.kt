package org.koolapp.web

import org.koolapp.template.*

import javax.servlet.annotation.WebListener
import javax.servlet.ServletContextListener
import javax.servlet.ServletContextEvent
import java.util.List
import kotlin.util.arrayList
import javax.servlet.ServletContext

[WebListener]
class ContextListener : ServletContextListener {

    override fun contextInitialized(event: ServletContextEvent?) {
        if (event != null) {
            val sc = event.getServletContext()
            if (sc != null) {
                sc.log("Stating the KoolApp ContextListener")
                for (filter in loadContextTextFilters(sc)) {
                    val name = filter.toString()

                    val servlet = TextFilterServlet(filter)
                    val registration = sc.addServlet(name, servlet)
                    if (registration != null) {
                        val mappings = filter.getUrlMapping()
                        sc.log("Adding filter: $filter with mappings: ${mappings.toList()}")
                        for (mapping in mappings) {
                            registration.addMapping(mapping)
                        }
                    }
                }
            }
        }
    }

    override fun contextDestroyed(event: ServletContextEvent?) {
    }

    protected fun loadContextTextFilters(sc: ServletContext): List<TextFilter> {
       return loadTextFilters(Thread.currentThread().sure().getContextClassLoader())
       //return loadTextFilters(sc.getClassLoader())
    }
}
