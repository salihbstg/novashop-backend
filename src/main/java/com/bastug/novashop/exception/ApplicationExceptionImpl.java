// Uygulama içinde özel hata fırlatmak için oluşturulan custom exception sınıfı
// RuntimeException'dan türediği için "unchecked exception" olur (try-catch zorunlu değil)
package com.bastug.novashop.exception;

public class ApplicationExceptionImpl extends RuntimeException {

    // Hata mesajını dışarıdan alıp üst sınıfa (RuntimeException) gönderir
    public ApplicationExceptionImpl(String message) {
        super(message);
    }
}