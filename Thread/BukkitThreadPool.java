package ru.Haber.CubeStream.Thread;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import ru.Haber.CubeStream.Parameters.ParametricFacade;
import ru.Haber.CubeStream.Parameters.PluginParamentrization;

public class BukkitThreadPool implements IBukkitThreadBuilder{
	
	private static final ParametricFacade param = PluginParamentrization.getDefaultFacade();

	 BukkitThreadPool() {
		
	}
	 
	 public static BukkitThreadPool pool() {
		 return new BukkitThreadPool();
	 }

	@Override
	public BukkitRunnable taskOf(@NotNull Runnable runnable) {
        return new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        };
	}
	@Contract(pure = true)
	@ParametersAreNonnullByDefault
	public BukkitTask timerTaskOf(@NotNull Runnable runnable, @NotNull long delay, @NotNull long period) {
		return taskOf(runnable).runTaskTimer(JavaPlugin.getPlugin(((Class<JavaPlugin>) param.getMainClass())), delay, period);
		
	}

	@Override
	public void sync(@NotNull Runnable runnable) {
		
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        }
        else {
            this.taskOf(runnable).runTask(JavaPlugin.getPlugin((Class<JavaPlugin>) param.getMainClass()));
        }	
		
	}

	@Override
	public void async(@NotNull Runnable runnable) {
		
        if (Bukkit.isPrimaryThread()) {
            this.taskOf(runnable).runTaskAsynchronously(JavaPlugin.getPlugin((Class<JavaPlugin>) param.getMainClass()));
        }
        else {
            runnable.run();
        }
		
	}

}
