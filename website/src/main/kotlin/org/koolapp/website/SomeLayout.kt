package org.koolapp.website

import org.koolapp.template.FilterContext
import org.koolapp.template.Template

class SomeLayout(val context: FilterContext): Template() {

    override fun render(out: Appendable): Unit {
        out.append("""
<html>
<title>My Layout</title>
<body>
${context.source.text()}
</body>
</html>
""")
    }
}