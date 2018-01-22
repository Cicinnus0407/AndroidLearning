// IBookManager.aidl
package com.cicinnus.androidlearning.binder;

// Declare any non-default types here with import statements

import com.cicinnus.androidlearning.binder.Book;
import com.cicinnus.androidlearning.binder.INewBookArrivedListener;
interface IBookManager {

     List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(in INewBookArrivedListener listener);

    void unRegisterListener(in INewBookArrivedListener listener);
}
