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
package com.grarak.kerneladiutor.utils.kernel.gpu;

import android.content.Context;

import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

/**
 * Created by Lonelyoneskatter on 24.09.17.
 */
public class TieredGPU {

    private static final String TIERED_GPU = "/sys/module/msm_kgsl_core/parameters";
    private static final String TIERED_GPU_PARAMETERS = "/sys/module/tiered_gpu_governor/parameters";
    private static final String TIERED_GPU_ACTIVATE = TIERED_GPU_PARAMETERS + "/tiered_gpu_activate";
    private static final String TIERED_GPU_EFFICIENCY_MODE = TIERED_GPU + "/tiered_efficiency_mode";
    private static final String TIERED_GPU_SAMPLE_TIME = TIERED_GPU + "/tiered_sample_time";

    public static void enableTieredGpu(boolean enable, Context context) {
        run(Control.write(enable ? "1" : "0", TIERED_GPU_ACTIVATE), TIERED_GPU_ACTIVATE, context);
    }

    public static boolean isTieredGpuEnabled() {
        return Utils.readFile(TIERED_GPU_ACTIVATE).equals("1");
    }

    public static boolean hasTieredGpuEnable() {
        return Utils.existFile(TIERED_GPU_ACTIVATE);
    }

    public static void enableTieredEfficiencyMode(boolean enable, Context context) {
        run(Control.write(enable ? "Y" : "N", TIERED_GPU_EFFICIENCY_MODE), TIERED_GPU_EFFICIENCY_MODE, context);
    }

    public static boolean isTieredEfficiencyModeEnabled() {
        return Utils.readFile(TIERED_GPU_EFFICIENCY_MODE).equals("Y");
    }

    public static boolean hasTieredEfficiencyModeEnable() {
        return Utils.existFile(TIERED_GPU_EFFICIENCY_MODE);
    }

    public static void setTieredGpuSampleTime(int value, Context context) {
        run(Control.write(String.valueOf(value * 1000), TIERED_GPU_SAMPLE_TIME), TIERED_GPU_SAMPLE_TIME, context);
    }

    public static int getTieredGpuSampleTime() {
        return Utils.strToInt(Utils.readFile(TIERED_GPU_SAMPLE_TIME)) / 1000;
    }

    public static boolean hasTieredGpuSampleTime() {
        return Utils.existFile(TIERED_GPU_SAMPLE_TIME);
    }

    public static boolean supported() {
        return Utils.existFile(TIERED_GPU_PARAMETERS);
    }

    private static void run(String command, String id, Context context) {
        Control.runSetting(command, ApplyOnBootFragment.GPU, id, context);
    }

}
