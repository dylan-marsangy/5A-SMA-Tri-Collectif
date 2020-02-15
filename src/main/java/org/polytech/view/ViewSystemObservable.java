package org.polytech.view;

public interface ViewSystemObservable {

    void notifyViewSystemObservers(ViewSystemObserver... observers);
}
