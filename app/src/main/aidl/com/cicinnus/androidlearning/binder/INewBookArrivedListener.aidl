// INewBookArrivedListener.aidl
package com.cicinnus.androidlearning.binder;


import com.cicinnus.androidlearning.binder.Book;
interface INewBookArrivedListener {
    void OnNewBookArrived(in Book newBook);

}
