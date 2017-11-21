package base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(value = ERROR_PATH)
    public String errorHandler(HttpServletRequest request, HttpServletResponse response) {
       int status = response.getStatus();
        if (403 == status) {
            return "<br/><h4>Requested path is forbidden</h4>";
        }
        return "<br/><h4>No such path known to server!</h4>";
    }
}
