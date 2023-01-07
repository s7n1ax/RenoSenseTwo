package me.zero.alpine.event.type;

public interface ICancellable {
    public void cancel();

    public boolean isCancelled();
}
