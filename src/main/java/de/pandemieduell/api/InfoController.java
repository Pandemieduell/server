package de.pandemieduell.api;

import de.pandemieduell.transferobjects.InfoTransferObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
  private final InfoTransferObject infoResponse;

  public InfoController(InfoTransferObject infoResponse) {
    this.infoResponse = infoResponse;
  }

  @GetMapping("/")
  public InfoTransferObject listEndpoints() {
    return infoResponse;
  }
}
