package com.webcon.breath.freescale.ui;

/**
 * Created by Administrator on 16-1-14.
 */
public interface IviewSpectro {

    /**
     * prepare data to draw graph
     * data preparation is in computation thread -->
     * so that the implementation of this interface should not draw UI directly but send message to UI to redraw for example:via Handler
     * @param ints :int array of breath signal
     * @param indicatorIndex : int array of indicators to be drawn, usually refined local mins to indicate breath cycles
     * @param timer : time elapsed after the appearance of one breath cycle
     * @param flux  : the flux of most recent breath cycle
     * @param signalState : the state of the signal computed by FFT,
     */
    void prepareGraphData(int[] ints, int[] indicatorIndex, int timer, int flux, int signalState);


}
