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
package com.grarak.kerneladiutor.utils.kernel.misc;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

/**
 * Created by willi on 30.06.16.
 */
public class PowerSuspend {

    private static final String PARENT = "/sys/kernel/power_suspend";
    private static final String ENABLED = PARENT + "/enabled";
    private static final String MODE = PARENT + "/mode";
    private static final String STATE = PARENT + "/state";
    private static final String VERSION = PARENT + "/version";

    public static void enableState(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", ENABLED), ENABLED, context);
    }

    public static boolean isStateEnabled() {
        return Utils.readFile(ENABLED).equals("1");
    }

    public static boolean hasState() {
        return Utils.existFile(ENABLED);
    }

    public static void setMode(int value, Context context) {
        run(Control.write(String.valueOf(value), MODE), MODE, context);
    }

    public static String getVersion() {
        return Utils.readFile(VERSION);
    }

    public static int getMode() {
        return Utils.strToInt(Utils.readFile(MODE));
    }

    public static boolean hasMode() {
        return Utils.existFile(MODE);
    }

    public static boolean supported() {
        return hasMode() || hasState();
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.MISC, id, context);
    }

}
