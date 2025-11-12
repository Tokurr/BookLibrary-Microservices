package com.library_book.library_service.client;

import com.library_book.library_service.exception.BookNotFoundException;
import com.library_book.library_service.exception.ExceptionMessage;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {


    private final ErrorDecoder errorDecoder = new Default();


    @Override
    public Exception decode(String s, Response response) {
        ExceptionMessage message = null;
        try (InputStream body = response.body().asInputStream()) {
            String bodyString = new String(body.readAllBytes(), StandardCharsets.UTF_8);
            message = new ExceptionMessage(
                    (String) response.headers().get("date").toArray()[0],
                    response.status(),
                    HttpStatus.resolve(response.status()).getReasonPhrase(),
                    bodyString,
                    response.request().url()
            );
        }

        catch (IOException exception)
        {
            return new Exception(message.getError());
        }
        switch (response.status()) {
            case 404:
                throw new BookNotFoundException(message);
            default:
                return errorDecoder.decode(s, response);
        }

    }


}
