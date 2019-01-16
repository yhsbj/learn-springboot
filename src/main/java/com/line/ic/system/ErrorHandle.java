package com.line.ic.system;

import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.line.util.dto.MsgDto;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorHandle extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    @Autowired
    public ErrorHandle(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes, Collections.emptyList());
        errorProperties = serverProperties.getError();
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<MsgDto> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        MsgDto dto = new MsgDto();
        dto.setStatus(status.value() / 100 * 100);
        try {
            dto.setMessage(getErrorMsg(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(dto, status);
    }

    private String getErrorMsg(HttpServletRequest request) {
        Throwable error = getAttribute(request, DefaultErrorAttributes.class.getName() + ".ERROR");
        if (error == null) {
            error = getAttribute(request, "javax.servlet.error.exception");
        }

        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = ((ServletException) error).getCause();
            }
        }

        BindingResult result = extractBindingResult(error);
        if (result != null) {
            if (!result.getAllErrors().isEmpty()) {
                return result.getAllErrors().get(0).getDefaultMessage();
            } else {
                return "服务忙，请稍后再试";
            }
        }

        String msg = error.getMessage();
        if (StringUtils.isEmpty(msg)) {
            msg = getAttribute(request, "javax.servlet.error.message");
        }

        return StringUtils.isEmpty(msg) ? "服务忙，请稍后再试" : msg;
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(HttpServletRequest request, String name) {
        return (T) request.getAttribute(name);
    }

}
