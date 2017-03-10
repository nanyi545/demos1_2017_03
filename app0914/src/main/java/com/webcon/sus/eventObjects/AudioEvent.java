package com.webcon.sus.eventObjects;

/**音频状态变化事件
 * @author m
 */
public class AudioEvent extends BaseEvent{
    public static final int AUDIO_EVENT_PLAY_COMPLETE = 1;

    public AudioEvent(int type) {
        super(type);
    }
}
