/*
 * Copyright (C) 2017 Lonelyoneskatter <threesixoh.skater@yahoo.com>
 *
 * This file is part of Kernel Adiutor.
 *
 * Kernel Adiutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kernel Adiutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kernel Adiutor.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.grarak.kerneladiutor.utils.kernel.cpuhotplug;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.cpu.CPUFreq;
import com.grarak.kerneladiutor.utils.root.Control;

/**
 * Created by Lonelyoneskatter on 09.22.2017.
 */
public class DynHotplug {

    private static final String DYN_HOTPLUG = "/sys/module/dynamic_hotplug/parameters";
    private static final String DYN_HOTPLUG_ENABLE = DYN_HOTPLUG + "/enabled";
    private static final String DYN_HOTPLUG_MIN_ONLINE = DYN_HOTPLUG + "/min_online";
    private static final String DYN_HOTPLUG_MAX_ONLINE = DYN_HOTPLUG + "/max_online";
    private static final String DYN_HOTPLUG_UP_THRESHOLD = DYN_HOTPLUG + "/up_threshold";
    private static final String DYN_HOTPLUG_UP_TIMER_CNT = DYN_HOTPLUG + "/up_timer_cnt";
    private static final String DYN_HOTPLUG_DOWN_TIMER_CNT = DYN_HOTPLUG + "/down_timer_cnt";

    public static void setDynHotplugDownTimerCnt(int value, Context context) {
        run(Control.write(String.valueOf(value), DYN_HOTPLUG_DOWN_TIMER_CNT),
                DYN_HOTPLUG_DOWN_TIMER_CNT, context);
    }

    public static int getDynHotplugDownTimerCnt() {
        return Utils.strToInt(Utils.readFile(DYN_HOTPLUG_DOWN_TIMER_CNT));
    }

    public static boolean hasDynHotplugDownTimerCnt() {
        return Utils.existFile(DYN_HOTPLUG_DOWN_TIMER_CNT);
    }

    public static void setDynHotplugUpTimerCnt(int value, Context context) {
        run(Control.write(String.valueOf(value), DYN_HOTPLUG_UP_TIMER_CNT),
                DYN_HOTPLUG_UP_TIMER_CNT, context);
    }

    public static int getDynHotplugUpTimerCnt() {
        return Utils.strToInt(Utils.readFile(DYN_HOTPLUG_UP_TIMER_CNT));
    }

    public static boolean hasDynHotplugUpTimerCnt() {
        return Utils.existFile(DYN_HOTPLUG_UP_TIMER_CNT);
    }

    public static void setDynHotplugUpThreshold(int value, Context context) {
        run(Control.write(String.valueOf(value), DYN_HOTPLUG_UP_THRESHOLD),
                DYN_HOTPLUG_UP_THRESHOLD, context);
    }

    public static int getDynHotplugUpThreshold() {
        return Utils.strToInt(Utils.readFile(DYN_HOTPLUG_UP_THRESHOLD));
    }

    public static boolean hasDynHotplugUpThreshold() {
        return Utils.existFile(DYN_HOTPLUG_UP_THRESHOLD);
    }

    public static void setDynHotplugMaxOnline(int value, Context context) {
        run(Control.write(String.valueOf(value), DYN_HOTPLUG_MAX_ONLINE),
                DYN_HOTPLUG_MAX_ONLINE, context);
    }

    public static int getDynHotplugMaxOnline() {
        return Utils.strToInt(Utils.readFile(DYN_HOTPLUG_MAX_ONLINE));
    }

    public static boolean hasDynHotplugMaxOnline() {
        return Utils.existFile(DYN_HOTPLUG_MAX_ONLINE);
    }

    public static void setDynHotplugMinOnline(int value, Context context) {
        run(Control.write(String.valueOf(value), DYN_HOTPLUG_MIN_ONLINE),
                DYN_HOTPLUG_MIN_ONLINE, context);
    }

    public static int getDynHotplugMinOnline() {
        return Utils.strToInt(Utils.readFile(DYN_HOTPLUG_MIN_ONLINE));
    }

    public static boolean hasDynHotplugMinOnline() {
        return Utils.existFile(DYN_HOTPLUG_MIN_ONLINE);
    }

    public static void enableDynHotplug(boolean enable, Context context) {
        run(Control.write(enable ? "Y" : "N", DYN_HOTPLUG_ENABLE), DYN_HOTPLUG_ENABLE, context);
    }

    public static boolean isDynHotplugEnabled() {
        return Utils.readFile(DYN_HOTPLUG_ENABLE).equals("Y");
    }

    public static boolean hasDynHotplugEnable() {
        return Utils.existFile(DYN_HOTPLUG_ENABLE);
    }

    public static boolean supported() {
        return Utils.existFile(DYN_HOTPLUG);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.CPU_HOTPLUG, id, context);
    }

}
