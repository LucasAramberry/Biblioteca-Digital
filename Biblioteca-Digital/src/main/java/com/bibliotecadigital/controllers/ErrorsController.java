package com.bibliotecadigital.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.Map;

/**
 * Controlador para gestionar errores
 *
 * @author Lucas Aramberry / aramberrylucas@gmail.com
 */
@Controller
public class ErrorsController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado.";
                break;
            }
            case 500: {
                errorMsg = "Ocurrio un error interno.";
                break;
            }
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {

        Map mapa = httpRequest.getParameterMap();
        for (Object key : mapa.keySet()) {
            String[] valores = (String[]) mapa.get(key);
        }
        Enumeration<String> atributos = httpRequest.getAttributeNames();
        while (atributos.hasMoreElements()) {
            String key = atributos.nextElement();
        }
        return (Integer) httpRequest.getAttribute("jakarta.servlet.error.status_code");
    }

    public String getErrorPath() {
        return "/error";
    }
}
