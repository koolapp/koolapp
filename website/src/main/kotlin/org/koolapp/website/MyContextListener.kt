package org.koolapp.website

import javax.servlet.annotation.WebListener
import org.koolapp.web.ContextListener
import javax.servlet.ServletContext
import org.koolapp.web.LayoutServletFilter

[WebListener]
public class MyContextListener() : ContextListener() {

    public override fun createLayoutFilter(sc: ServletContext): LayoutServletFilter? {
        return MyLayoutFilter()
    }
}
