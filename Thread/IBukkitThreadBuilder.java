package ru.Haber.CubeStream.Thread;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;



public interface IBukkitThreadBuilder{
	
	BukkitRunnable taskOf(@NotNull Runnable runnable);
	
	void sync(@NotNull Runnable runnable);
	
	void async(@NotNull Runnable runnable);
	
   default <U> CompletableFuture<Void> completeVoid(final Runnable runnable) {
        final CompletableFuture<Void> completableFuture = new CompletableFuture<Void>();
        this.async(() -> {
            try {
                runnable.run();
                this.sync(() -> completableFuture.complete(null));
            }
            catch (Exception ex) {
                completableFuture.completeExceptionally(ex);
            }
            return;
        });
        return completableFuture;
    }
   
  default <U> CompletableFuture<U> complete(final Supplier<U> supplier) {
       final CompletableFuture<U> completableFuture = new CompletableFuture<U>();
       this.async(() -> {
           try {
               this.sync(() -> completableFuture.complete(supplier.get()));
           }
           catch (Exception ex) {
               completableFuture.completeExceptionally(ex);
           }
           return;
       });
       return completableFuture;
   }

}
