/*
 * Copyright (C) 2017 Willi Ye <williye97@gmail.com>
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
 * Created by willi on 25.09.17.
 */
public class StateNotifier {

    private static final String STATE_NOTIFIER = "/sys/module/state_notifier/parameters";
    private static final String STATE_NOTIFIER_ENABLE = STATE_NOTIFIER + "/enabled";

    public static void enableStateNotifier(boolean enable, Context context) {
        run(Control.write(enable ? "Y" : "N", STATE_NOTIFIER_ENABLE), STATE_NOTIFIER_ENABLE, context);
    }

    public static boolean isStateNotifierEnabled() {
        return Utils.readFile(STATE_NOTIFIER_ENABLE).equals("Y");
    }

    public static boolean hasStateNotifierEnable() {
        return Utils.existFile(STATE_NOTIFIER_ENABLE);
    }

    public static boolean supported() {
        return Utils.existFile(STATE_NOTIFIER);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.MISC, id, context);
    }

}
