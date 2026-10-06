package com.rodrigo.core.oop.workspace2;

public record Book(String title, String author, String description, int pages) {
    public String printBook() {
        return "The book is: " + title + " and the author is: " + author + " and the pages is: " + pages ;
    }
}
