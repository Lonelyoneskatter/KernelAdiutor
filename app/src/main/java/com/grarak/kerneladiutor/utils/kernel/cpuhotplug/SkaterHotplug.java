/*
 * Copyright (C) 2015-2016 Willi Ye <williye97@gmail.com>
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
import com.grarak.kerneladiutor.utils.kernel.cpu.CPUFreq;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

/**
 * Created by willi on 11.05.16.
 */
public class SkaterHotplug {

    private static final String SKATER_HOTPLUG = "/sys/kernel/skater_hotplug";
    private static final String SKATER_HOTPLUG_PARAMETERS = "/sys/module/skater_hotplug/parameters";
    private static final String SKATER_HOTPLUG_ENABLE = SKATER_HOTPLUG_PARAMETERS + "/enabled";
    private static final String SKATER_HOTPLUG_BOOST_LOCK_DUR = SKATER_HOTPLUG + "/boost_lock_duration";
    private static final String SKATER_HOTPLUG_CPUS_BOOSTED = SKATER_HOTPLUG + "/cpus_boosted";
    private static final String SKATER_HOTPLUG_CPUFREQ_DOWN = SKATER_HOTPLUG + "/cpufreq_down";
    private static final String SKATER_HOTPLUG_CPUFREQ_UP = SKATER_HOTPLUG + "/cpufreq_up";
    private static final String SKATER_HOTPLUG_CYCLE_DOWN = SKATER_HOTPLUG + "/cycle_down";
    private static final String SKATER_HOTPLUG_CYCLE_UP = SKATER_HOTPLUG + "/cycle_up";
    private static final String SKATER_HOTPLUG_DELAY = SKATER_HOTPLUG + "/delay";
    private static final String SKATER_HOTPLUG_MAX_CPUS = SKATER_HOTPLUG + "/max_cpus";
    private static final String SKATER_HOTPLUG_MIN_BOOST_FREQ = SKATER_HOTPLUG + "/min_boost_freq";
    private static final String SKATER_HOTPLUG_MIN_CPUS = SKATER_HOTPLUG + "/min_cpus";

    public static void setSkaterHotplugMinCpus(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_MIN_CPUS),
                SKATER_HOTPLUG_MIN_CPUS, context);
    }

    public static int getSkaterHotplugMinCpus() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_MIN_CPUS));
    }

    public static boolean hasSkaterHotplugMinCpus() {
        return Utils.existFile(SKATER_HOTPLUG_MIN_CPUS);
    }

    public static void setSkaterHotplugMaxCpus(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_MAX_CPUS),
                SKATER_HOTPLUG_MAX_CPUS, context);
    }

    public static int getSkaterHotplugMaxCpus() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_MAX_CPUS));
    }

    public static boolean hasSkaterHotplugMaxCpus() {
        return Utils.existFile(SKATER_HOTPLUG_MAX_CPUS);
    }

    public static void setSkaterHotplugDelay(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_DELAY), SKATER_HOTPLUG_DELAY, context);
    }

    public static int getSkaterHotplugDelay() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_DELAY));
    }

    public static boolean hasSkaterHotplugDelay() {
        return Utils.existFile(SKATER_HOTPLUG_DELAY);
    }

    public static void setSkaterHotplugCycleUp(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_CYCLE_UP),
                SKATER_HOTPLUG_CYCLE_UP, context);
    }

    public static int getSkaterHotplugCycleUp() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_CYCLE_UP));
    }

    public static boolean hasSkaterHotplugCycleUp() {
        return Utils.existFile(SKATER_HOTPLUG_CYCLE_UP);
    }

    public static void setSkaterHotplugCycleDown(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_CYCLE_DOWN),
                SKATER_HOTPLUG_CYCLE_DOWN, context);
    }

    public static int getSkaterHotplugCycleDown() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_CYCLE_DOWN));
    }

    public static boolean hasSkaterHotplugCycleDown() {
        return Utils.existFile(SKATER_HOTPLUG_CYCLE_DOWN);
    }

    public static void setSkaterHotplugCpufreqUp(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_CPUFREQ_UP),
                SKATER_HOTPLUG_CPUFREQ_UP, context);
    }

    public static int getSkaterHotplugCpufreqUp() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_CPUFREQ_UP));
    }

    public static boolean hasSkaterHotplugCpufreqUp() {
        return Utils.existFile(SKATER_HOTPLUG_CPUFREQ_UP);
    }

    public static void setSkaterHotplugCpufreqDown(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_CPUFREQ_DOWN),
                SKATER_HOTPLUG_CPUFREQ_DOWN, context);
    }

    public static int getSkaterHotplugCpufreqDown() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_CPUFREQ_DOWN));
    }

    public static boolean hasSkaterHotplugCpufreqDown() {
        return Utils.existFile(SKATER_HOTPLUG_CPUFREQ_DOWN);
    }

    public static void setSkaterHotplugMinBoostFreq(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_MIN_BOOST_FREQ),
                SKATER_HOTPLUG_MIN_BOOST_FREQ, context);
    }

    public static int getSkaterHotplugMinBoostFreq() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_MIN_BOOST_FREQ));
    }

    public static boolean hasSkaterHotplugMinBoostFreq() {
        return Utils.existFile(SKATER_HOTPLUG_MIN_BOOST_FREQ);
    }

    public static void setSkaterHotplugBoostLockDuration(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_BOOST_LOCK_DUR),
                SKATER_HOTPLUG_BOOST_LOCK_DUR, context);
    }

    public static int getSkaterHotplugBoostLockDuration() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_BOOST_LOCK_DUR));
    }

    public static boolean hasSkaterHotplugBoostLockDuration() {
        return Utils.existFile(SKATER_HOTPLUG_BOOST_LOCK_DUR);
    }

    public static void setSkaterHotplugCpusBoosted(int value, Context context) {
        run(Control.write(String.valueOf(value), SKATER_HOTPLUG_CPUS_BOOSTED),
                SKATER_HOTPLUG_CPUS_BOOSTED, context);
    }

    public static int getSkaterHotplugCpusBoosted() {
        return Utils.strToInt(Utils.readFile(SKATER_HOTPLUG_CPUS_BOOSTED));
    }

    public static boolean hasSkaterHotplugCpusBoosted() {
        return Utils.existFile(SKATER_HOTPLUG_CPUS_BOOSTED);
    }

    public static void enableSkaterHotplug(boolean enable, Context context) {
        run(Control.write(enable ? "Y" : "N", SKATER_HOTPLUG_ENABLE), SKATER_HOTPLUG_ENABLE, context);
    }

    public static boolean isSkaterHotplugEnabled() {
        return Utils.readFile(SKATER_HOTPLUG_ENABLE).equals("Y");
    }

    public static boolean hasSkaterHotplugEnable() {
        return Utils.existFile(SKATER_HOTPLUG_ENABLE);
    }

    public static boolean supported() {
        return Utils.existFile(SKATER_HOTPLUG_PARAMETERS) || Utils.existFile(SKATER_HOTPLUG);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.CPU_HOTPLUG, id, context);
    }

}
