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
                for (filter in loadContextTextFilters(sc)) {
                    val name = filter.toString()
                    val servlet = TextFilterServlet(filter)
                    val registration = sc.addServlet(name, servlet)
                    if (registration != null) {
                        for (mapping in filter.urlMapping) {
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
       return loadTextFilters(sc.getClassLoader())
    }
}
