package org.koolapp.website

import javax.servlet.annotation.WebListener
import org.koolapp.web.ContextListener
import javax.servlet.ServletContext
import org.koolapp.web.LayoutFilter

[WebListener]
public class MyContextListener() : ContextListener() {

    override fun createLayoutFilter(sc: ServletContext): LayoutFilter? {
        return MyLayout()
    }
}
