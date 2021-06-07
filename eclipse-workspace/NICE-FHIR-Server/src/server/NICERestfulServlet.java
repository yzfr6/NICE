package server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import resource_providers.Medication_ResourceProvider;
import resource_providers.MedicinalProductContraindication_ResourceProvider;
import resource_providers.MedicinalProductUndesirableEffect_ResourceProvider;

/**
 * In this example, we are using Servlet 3.0 annotations to define
 * the URL pattern for this servlet, but we could also
 * define this in a web.xml file.
 */
@WebServlet(urlPatterns= {"/fhir/*"})
public class NICERestfulServlet extends RestfulServer {

   private static final long serialVersionUID = 1L;

   
   
   /**
    * The initialize method is automatically called when the servlet is starting up, so it can
    * be used to configure the servlet to define resource providers, or set up
    * configuration, interceptors, etc.
    */
   @Override
   protected void initialize() throws ServletException {
      
	   System.out.println("------------Server initialised");
      List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
      resourceProviders.add(new Medication_ResourceProvider());
      resourceProviders.add(new MedicinalProductUndesirableEffect_ResourceProvider());
      resourceProviders.add(new MedicinalProductContraindication_ResourceProvider());
      setResourceProviders(resourceProviders);
   }
   
}
