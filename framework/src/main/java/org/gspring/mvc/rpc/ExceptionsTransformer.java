package org.gspring.mvc.rpc;

/**
 * Transformer between server side exceptions and client side aware exceptions.
 *
 * Example for:
 * org.springframework.security.access.AccessDeniedException
 * it could rethrow GWT client aware exceptions
 */
public interface ExceptionsTransformer {
  RuntimeException transform(Exception serverSideError);
}
