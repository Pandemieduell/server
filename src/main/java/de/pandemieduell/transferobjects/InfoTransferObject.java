package de.pandemieduell.transferobjects;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class InfoTransferObject {
  private final List<String> endpoints = new ArrayList<>();

  public InfoTransferObject(RequestMappingHandlerMapping requestHandlerMapping) {
    var conditions = requestHandlerMapping.getHandlerMethods().keySet();
    for (var condition : conditions) {
      for (var method : condition.getMethodsCondition().getMethods()) {
        for (var path : condition.getPatternsCondition().getPatterns()) {
          endpoints.add(method + " " + path);
        }
      }
    }
  }

  public List<String> getEndpoints() {
    return endpoints;
  }

  public String getInfo() {
    return "Pandemieduell API";
  }

  public String getMessage() {
    return "Stay healhty!";
  }
}
