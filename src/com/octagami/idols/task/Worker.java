package com.octagami.idols.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.octagami.idols.IdolsPlugin;


public class Worker implements Runnable {

    public static IdolsPlugin plugin;

    public Worker(IdolsPlugin instance) {

        plugin = instance;
    }

    @Override
    public void run() {

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            public void run() {
                plugin.getServer().broadcastMessage("This message is broadcast by the main thread");
            }
        }, 60L);

        // plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin,
        // new Runnable() {
        //
        // public void run() {
        // System.out.println("This message is printed by an async thread");
        // }
        //
        // }, 60L, 200L);

        Future<String> returnFuture = plugin.getServer().getScheduler().callSyncMethod(plugin, new Callable<String>() {

            public String call() {
                return "This is the string to return";
            }

        });

        try {
            // This will block the current thread
            String returnValue = returnFuture.get();
            System.out.println(returnValue);
        } catch (InterruptedException e) {
            System.out.println("Interrupt triggered which waiting on callable to return");
        } catch (ExecutionException e) {
            System.out.println("Callable task threw an exception");
            e.getCause().printStackTrace();
        }

    }

}
