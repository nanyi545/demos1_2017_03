package com.webcon.dataProcess;

/**
 * Created by Administrator on 16-1-21.
 *
 * specify actions to do upon identification of a new breath cycle
 */
public interface NewCycleAction {

    void doAction(int[] currentBreathSignal, int[] localMins_refined, int[] breathFlux);

}
