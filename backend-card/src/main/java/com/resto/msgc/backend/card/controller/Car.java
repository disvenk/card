package com.resto.msgc.backend.card.controller;

import java.util.function.Supplier;

/**
 * Created by disvenk.dai on 2018-04-28 10:24
 */
public class Car {

    public static void main(String[] args){
        Integer a = 1500;
       System.out.println(a==1500);
    }

    public static Car create( final Supplier< Car > supplier ) {
                   return supplier.get();
                 }

                public static void collide( final Car car ) {
                     System.out.println( "静态方法引用 " + car.toString() );
              }

               public void repair() {
                    System.out.println( "任意对象的方法引用 " + this.toString() );
           }

                 public void follow( final Car car ) {
                    System.out.println( "特定对象的方法引用 " + car.toString() );
            }

}
