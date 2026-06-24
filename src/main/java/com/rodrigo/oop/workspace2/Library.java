package com.rodrigo.oop.workspace2;

public class Library {
    static void main(String[] args) {
        Book book = new Book("The Kybalion", "Hermes Trismegistus", "A hermetic book", 100);
        String newBook = book.printBook();
        System.out.println("The book is: " + newBook);

        System.out.println(book.description());
    }
}
