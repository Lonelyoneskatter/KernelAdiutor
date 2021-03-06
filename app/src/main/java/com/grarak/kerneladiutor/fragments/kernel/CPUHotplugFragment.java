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
package com.grarak.kerneladiutor.fragments.kernel;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.fragments.ApplyOnBootFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.cpu.CPUFreq;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.AiOHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.AlucardHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.AutoSmp;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.BluPlug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.DynHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.CoreCtl;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.IntelliPlug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.LazyPlug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.MBHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.MPDecision;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.MSMHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.MakoHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.SkaterHotplug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.ThunderPlug;
import com.grarak.kerneladiutor.utils.kernel.cpuhotplug.ZenDecision;
import com.grarak.kerneladiutor.views.recyclerview.DescriptionView;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.SelectView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;
import com.grarak.kerneladiutor.views.recyclerview.TitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by willi on 07.05.16.
 */
public class CPUHotplugFragment extends RecyclerViewFragment {

    private List<SwitchView> mEnableViews = new ArrayList<>();

    @Override
    protected void init() {
        super.init();

        addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        mEnableViews.clear();

        if (MPDecision.supported()) {
            mpdecisionInit(items);
        }
        if (IntelliPlug.supported()) {
            intelliPlugInit(items);
        }
        if (LazyPlug.supported()) {
            lazyPlugInit(items);
        }
        if (BluPlug.supported()) {
            bluPlugInit(items);
        }
        if (DynHotplug.supported()) {
            dynHotplugInit(items);
        }
        if (MSMHotplug.supported()) {
            msmHotplugInit(items);
        }
        if (MakoHotplug.supported()) {
            makoHotplugInit(items);
        }
        if (MBHotplug.supported()) {
            mbHotplugInit(items);
        }
        if (AlucardHotplug.supported()) {
            alucardHotplugInit(items);
        }
        if (SkaterHotplug.supported()) {
            skaterHotplugInit(items);
        }
        if (ThunderPlug.supported()) {
            thunderPlugInit(items);
        }
        if (ZenDecision.supported()) {
            zenDecisionInit(items);
        }
        if (AutoSmp.supported()) {
            autoSmpInit(items);
        }
        if (CoreCtl.supported()) {
            coreCtlInit(items);
        }
        if (AiOHotplug.supported()) {
            aioHotplugInit(items);
        }

        for (SwitchView view : mEnableViews) {
            view.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    boolean enabled = false;
                    for (SwitchView view : mEnableViews) {
                        if (!enabled && view.isChecked()) {
                            enabled = true;
                            continue;
                        }
                        if (enabled && view.isChecked()) {
                            Utils.toast(R.string.hotplug_warning, getActivity());
                            break;
                        }
                    }
                }
            });
        }
    }

    private void mpdecisionInit(List<RecyclerViewItem> items) {
        SwitchView mpdecision = new SwitchView();
        mpdecision.setTitle(getString(R.string.mpdecision));
        mpdecision.setSummary(getString(R.string.mpdecision_summary));
        mpdecision.setChecked(MPDecision.isMpdecisionEnabled());
        mpdecision.addOnSwitchListener(new SwitchView.OnSwitchListener() {
            @Override
            public void onChanged(SwitchView switchView, boolean isChecked) {
                MPDecision.enableMpdecision(isChecked, getActivity());
            }
        });

        items.add(mpdecision);
        mEnableViews.add(mpdecision);
    }

    private void intelliPlugInit(List<RecyclerViewItem> items) {
        CardView intelliplug = new CardView(getActivity());
        intelliplug.setTitle(getString(R.string.intelliplug));

        if (IntelliPlug.hasIntelliPlugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.intelliplug));
            enable.setSummary(getString(R.string.intelliplug_summary));
            enable.setChecked(IntelliPlug.isIntelliPlugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    IntelliPlug.enableIntelliPlug(isChecked, getActivity());
                }
            });

            intelliplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (IntelliPlug.hasIntelliPlugProfile()) {
            SelectView profile = new SelectView();
            profile.setTitle(getString(R.string.profile));
            profile.setSummary(getString(R.string.cpu_hotplug_profile_summary));
            profile.setItems(IntelliPlug.getIntelliPlugProfileMenu(getActivity()));
            profile.setItem(IntelliPlug.getIntelliPlugProfile());
            profile.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    IntelliPlug.setIntelliPlugProfile(position, getActivity());
                }
            });

            intelliplug.addItem(profile);
        }

        if (IntelliPlug.hasIntelliPlugEco()) {
            SwitchView eco = new SwitchView();
            eco.setTitle(getString(R.string.eco_mode));
            eco.setSummary(getString(R.string.eco_mode_summary));
            eco.setChecked(IntelliPlug.isIntelliPlugEcoEnabled());
            eco.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    IntelliPlug.enableIntelliPlugEco(isChecked, getActivity());
                }
            });

            intelliplug.addItem(eco);
        }

        if (IntelliPlug.hasIntelliPlugTouchBoost()) {
            SwitchView touchBoost = new SwitchView();
            touchBoost.setTitle(getString(R.string.touch_boost));
            touchBoost.setSummary(getString(R.string.touch_boost_summary));
            touchBoost.setChecked(IntelliPlug.isIntelliPlugTouchBoostEnabled());
            touchBoost.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    IntelliPlug.enableIntelliPlugTouchBoost(isChecked, getActivity());
                }
            });

            intelliplug.addItem(touchBoost);
        }

        if (IntelliPlug.hasIntelliPlugHysteresis()) {
            SeekBarView hysteresis = new SeekBarView();
            hysteresis.setTitle(getString(R.string.hysteresis));
            hysteresis.setSummary(getString(R.string.hysteresis_summary));
            hysteresis.setMax(17);
            hysteresis.setProgress(IntelliPlug.getIntelliPlugHysteresis());
            hysteresis.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugHysteresis(position, getActivity());
                }
            });

            intelliplug.addItem(hysteresis);
        }

        if (IntelliPlug.hasIntelliPlugThresold()) {
            SeekBarView threshold = new SeekBarView();
            threshold.setTitle(getString(R.string.cpu_threshold));
            threshold.setSummary(getString(R.string.cpu_threshold_summary));
            threshold.setMax(1000);
            threshold.setProgress(IntelliPlug.getIntelliPlugThresold());
            threshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugThresold(position, getActivity());
                }
            });

            intelliplug.addItem(threshold);
        }

        if (IntelliPlug.hasIntelliPlugScreenOffMax() && CPUFreq.getFreqs() != null) {
            List<String> list = new ArrayList<>();
            list.add(getString(R.string.disabled));
            list.addAll(CPUFreq.getAdjustedFreq(getActivity()));

            SelectView maxScreenOffFreq = new SelectView();
            maxScreenOffFreq.setTitle(getString(R.string.cpu_max_screen_off_freq));
            maxScreenOffFreq.setSummary(getString(R.string.cpu_max_screen_off_freq_summary));
            maxScreenOffFreq.setItems(list);
            maxScreenOffFreq.setItem(IntelliPlug.getIntelliPlugScreenOffMax());
            maxScreenOffFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    IntelliPlug.setIntelliPlugScreenOffMax(position, getActivity());
                }
            });

            intelliplug.addItem(maxScreenOffFreq);
        }

        if (IntelliPlug.hasIntelliPlugDebug()) {
            SwitchView debug = new SwitchView();
            debug.setTitle(getString(R.string.debug_mask));
            debug.setSummary(getString(R.string.debug_mask_summary));
            debug.setChecked(IntelliPlug.isIntelliPlugDebugEnabled());
            debug.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    IntelliPlug.enableIntelliPlugDebug(isChecked, getActivity());
                }
            });

            intelliplug.addItem(debug);
        }

        if (IntelliPlug.hasIntelliPlugSuspend()) {
            SwitchView suspend = new SwitchView();
            suspend.setTitle(getString(R.string.suspend));
            suspend.setSummary(getString(R.string.suspend_summary));
            suspend.setChecked(IntelliPlug.isIntelliPlugSuspendEnabled());
            suspend.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    IntelliPlug.enableIntelliPlugSuspend(isChecked, getActivity());
                }
            });

            intelliplug.addItem(suspend);
        }

        if (IntelliPlug.hasIntelliPlugCpusBoosted()) {
            SeekBarView cpusBoosted = new SeekBarView();
            cpusBoosted.setTitle(getString(R.string.cpus_boosted));
            cpusBoosted.setSummary(getString(R.string.cpus_boosted_summary));
            cpusBoosted.setMax(CPUFreq.getCpuCount());
            cpusBoosted.setMin(1);
            cpusBoosted.setProgress(IntelliPlug.getIntelliPlugCpusBoosted() - 1);
            cpusBoosted.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugCpusBoosted(position + 1, getActivity());
                }
            });

            intelliplug.addItem(cpusBoosted);
        }

        if (IntelliPlug.hasIntelliPlugMinCpusOnline()) {
            SeekBarView minCpusOnline = new SeekBarView();
            minCpusOnline.setTitle(getString(R.string.min_cpu_online));
            minCpusOnline.setSummary(getString(R.string.min_cpu_online_summary));
            minCpusOnline.setMax(CPUFreq.getCpuCount());
            minCpusOnline.setMin(1);
            minCpusOnline.setProgress(IntelliPlug.getIntelliPlugMinCpusOnline() - 1);
            minCpusOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugMinCpusOnline(position + 1, getActivity());
                }
            });

            intelliplug.addItem(minCpusOnline);
        }

        if (IntelliPlug.hasIntelliPlugMaxCpusOnline()) {
            SeekBarView maxCpusOnline = new SeekBarView();
            maxCpusOnline.setTitle(getString(R.string.max_cpu_online));
            maxCpusOnline.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpusOnline.setMax(CPUFreq.getCpuCount());
            maxCpusOnline.setMin(1);
            maxCpusOnline.setProgress(IntelliPlug.getIntelliPlugMaxCpusOnline() - 1);
            maxCpusOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugMaxCpusOnline(position + 1, getActivity());
                }
            });

            intelliplug.addItem(maxCpusOnline);
        }

        if (IntelliPlug.hasIntelliPlugMaxCpusOnlineSusp()) {
            SeekBarView maxCpusOnlineSusp = new SeekBarView();
            maxCpusOnlineSusp.setTitle(getString(R.string.max_cpu_online_screen_off));
            maxCpusOnlineSusp.setSummary(getString(R.string.max_cpu_online_screen_off_summary));
            maxCpusOnlineSusp.setMax(CPUFreq.getCpuCount());
            maxCpusOnlineSusp.setMin(1);
            maxCpusOnlineSusp.setProgress(IntelliPlug.getIntelliPlugMaxCpusOnlineSusp() - 1);
            maxCpusOnlineSusp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugMaxCpusOnlineSusp(position + 1, getActivity());
                }
            });

            intelliplug.addItem(maxCpusOnlineSusp);
        }

        if (IntelliPlug.hasIntelliPlugSuspendDeferTime()) {
            SeekBarView suspendDeferTime = new SeekBarView();
            suspendDeferTime.setTitle(getString(R.string.suspend_defer_time));
            suspendDeferTime.setUnit(getString(R.string.ms));
            suspendDeferTime.setMax(5000);
            suspendDeferTime.setOffset(10);
            suspendDeferTime.setProgress(IntelliPlug.getIntelliPlugSuspendDeferTime() / 10);
            suspendDeferTime.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugSuspendDeferTime(position * 10, getActivity());
                }
            });

            intelliplug.addItem(suspendDeferTime);
        }

        if (IntelliPlug.hasIntelliPlugDeferSampling()) {
            SeekBarView deferSampling = new SeekBarView();
            deferSampling.setTitle(getString(R.string.defer_sampling));
            deferSampling.setUnit(getString(R.string.ms));
            deferSampling.setMax(1000);
            deferSampling.setProgress(IntelliPlug.getIntelliPlugDeferSampling());
            deferSampling.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugDeferSampling(position, getActivity());
                }
            });

            intelliplug.addItem(deferSampling);
        }

        if (IntelliPlug.hasIntelliPlugBoostLockDuration()) {
            SeekBarView boostLockDuration = new SeekBarView();
            boostLockDuration.setTitle(getString(R.string.boost_lock_duration));
            boostLockDuration.setSummary(getString(R.string.boost_lock_duration_summary));
            boostLockDuration.setUnit(getString(R.string.ms));
            boostLockDuration.setMax(5000);
            boostLockDuration.setMin(1);
            boostLockDuration.setProgress(IntelliPlug.getIntelliPlugBoostLockDuration() - 1);
            boostLockDuration.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugBoostLockDuration(position + 1, getActivity());
                }
            });

            intelliplug.addItem(boostLockDuration);
        }

        if (IntelliPlug.hasIntelliPlugDownLockDuration()) {
            SeekBarView downLockDuration = new SeekBarView();
            downLockDuration.setTitle(getString(R.string.down_lock_duration));
            downLockDuration.setSummary(getString(R.string.down_lock_duration_summary));
            downLockDuration.setUnit(getString(R.string.ms));
            downLockDuration.setMax(5000);
            downLockDuration.setMin(1);
            downLockDuration.setProgress(IntelliPlug.getIntelliPlugDownLockDuration() - 1);
            downLockDuration.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugDownLockDuration(position + 1, getActivity());
                }
            });

            intelliplug.addItem(downLockDuration);
        }

        if (IntelliPlug.hasIntelliPlugFShift()) {
            SeekBarView fShift = new SeekBarView();
            fShift.setTitle(getString(R.string.fshift));
            fShift.setMax(4);
            fShift.setProgress(IntelliPlug.getIntelliPlugFShift());
            fShift.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    IntelliPlug.setIntelliPlugFShift(position, getActivity());
                }
            });

            intelliplug.addItem(fShift);
        }

        if (intelliplug.size() > 0) {
            items.add(intelliplug);
        }
    }

    private void lazyPlugInit(List<RecyclerViewItem> items) {
        CardView lazyplug = new CardView(getActivity());
        lazyplug.setTitle(getString(R.string.lazyplug));

        if (LazyPlug.hasEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.lazyplug));
            enable.setSummary(getString(R.string.lazyplug_summary));
            enable.setChecked(LazyPlug.isEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    LazyPlug.enable(isChecked, getActivity());
                }
            });

            lazyplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (LazyPlug.hasProfile()) {
            SelectView profile = new SelectView();
            profile.setTitle(getString(R.string.profile));
            profile.setSummary(getString(R.string.cpu_hotplug_profile_summary));
            profile.setItems(LazyPlug.getProfileMenu(getActivity()));
            profile.setItem(LazyPlug.getProfile());
            profile.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    LazyPlug.setProfile(position, getActivity());
                }
            });

            lazyplug.addItem(profile);
        }

        if (LazyPlug.hasTouchBoost()) {
            SwitchView touchBoost = new SwitchView();
            touchBoost.setTitle(getString(R.string.touch_boost));
            touchBoost.setSummary(getString(R.string.touch_boost_summary));
            touchBoost.setChecked(LazyPlug.isTouchBoostEnabled());
            touchBoost.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    LazyPlug.enableTouchBoost(isChecked, getActivity());
                }
            });

            lazyplug.addItem(touchBoost);
        }

        if (LazyPlug.hasHysteresis()) {
            SeekBarView hysteresis = new SeekBarView();
            hysteresis.setTitle(getString(R.string.hysteresis));
            hysteresis.setSummary(getString(R.string.hysteresis_summary));
            hysteresis.setMax(17);
            hysteresis.setProgress(LazyPlug.getHysteresis());
            hysteresis.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    LazyPlug.setHysteresis(position, getActivity());
                }
            });

            lazyplug.addItem(hysteresis);
        }

        if (LazyPlug.hasThreshold()) {
            SeekBarView threshold = new SeekBarView();
            threshold.setTitle(getString(R.string.cpu_threshold));
            threshold.setSummary(getString(R.string.cpu_threshold_summary));
            threshold.setMax(1250);
            threshold.setProgress(LazyPlug.getThreshold());
            threshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    LazyPlug.setThreshold(position, getActivity());
                }
            });

            lazyplug.addItem(threshold);
        }

        if (LazyPlug.hasPossibleCores()) {
            SeekBarView possibleCores = new SeekBarView();
            possibleCores.setTitle(getString(R.string.max_cpu_online));
            possibleCores.setSummary(getString(R.string.possible_cpu_cores_summary));
            possibleCores.setMax(CPUFreq.getCpuCount());
            possibleCores.setMin(1);
            possibleCores.setProgress(LazyPlug.getPossibleCores() - 1);
            possibleCores.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    LazyPlug.setPossibleCores(position + 1, getActivity());
                }
            });

            lazyplug.addItem(possibleCores);
        }

        if (lazyplug.size() > 0) {
            items.add(lazyplug);
        }
    }

    private void bluPlugInit(List<RecyclerViewItem> items) {
        final CardView bluplug = new CardView(getActivity());
        bluplug.setTitle(getString(R.string.blu_plug));

        if (BluPlug.hasBluPlugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.blu_plug));
            enable.setSummary(getString(R.string.blu_plug_summary));
            enable.setChecked(BluPlug.isBluPlugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    BluPlug.enableBluPlug(isChecked, getActivity());
                }
            });

            bluplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (BluPlug.hasBluPlugPowersaverMode()) {
            SwitchView powersaverMode = new SwitchView();
            powersaverMode.setTitle(getString(R.string.powersaver_mode));
            powersaverMode.setSummary(getString(R.string.powersaver_mode_summary));
            powersaverMode.setChecked(BluPlug.isBluPlugPowersaverModeEnabled());
            powersaverMode.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    BluPlug.enableBluPlugPowersaverMode(isChecked, getActivity());
                }
            });

            bluplug.addItem(powersaverMode);
        }

        if (BluPlug.hasBluPlugMinOnline()) {
            SeekBarView minOnline = new SeekBarView();
            minOnline.setTitle(getString(R.string.min_cpu_online));
            minOnline.setSummary(getString(R.string.min_cpu_online_summary));
            minOnline.setMax(CPUFreq.getCpuCount());
            minOnline.setMin(1);
            minOnline.setProgress(BluPlug.getBluPlugMinOnline() - 1);
            minOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugMinOnline(position + 1, getActivity());
                }
            });

            bluplug.addItem(minOnline);
        }

        if (BluPlug.hasBluPlugMaxOnline()) {
            SeekBarView maxOnline = new SeekBarView();
            maxOnline.setTitle(getString(R.string.max_cpu_online));
            maxOnline.setSummary(getString(R.string.max_cpu_online_summary));
            maxOnline.setMax(CPUFreq.getCpuCount());
            maxOnline.setMin(1);
            maxOnline.setProgress(BluPlug.getBluPlugMaxOnline() - 1);
            maxOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugMaxOnline(position + 1, getActivity());
                }
            });

            bluplug.addItem(maxOnline);
        }

        if (BluPlug.hasBluPlugMaxCoresScreenOff()) {
            SeekBarView maxCoresScreenOff = new SeekBarView();
            maxCoresScreenOff.setTitle(getString(R.string.max_cpu_online_screen_off));
            maxCoresScreenOff.setSummary(getString(R.string.max_cpu_online_screen_off_summary));
            maxCoresScreenOff.setMax(CPUFreq.getCpuCount());
            maxCoresScreenOff.setMin(1);
            maxCoresScreenOff.setProgress(BluPlug.getBluPlugMaxCoresScreenOff() - 1);
            maxCoresScreenOff.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugMaxCoresScreenOff(position + 1, getActivity());
                }
            });

            bluplug.addItem(maxCoresScreenOff);
        }

        if (BluPlug.hasBluPlugMaxFreqScreenOff() && CPUFreq.getFreqs() != null) {
            List<String> list = new ArrayList<>();
            list.add(getString(R.string.disabled));
            list.addAll(CPUFreq.getAdjustedFreq(getActivity()));

            SeekBarView maxFreqScreenOff = new SeekBarView();
            maxFreqScreenOff.setTitle(getString(R.string.cpu_max_screen_off_freq));
            maxFreqScreenOff.setSummary(getString(R.string.cpu_max_screen_off_freq_summary));
            maxFreqScreenOff.setItems(list);
            maxFreqScreenOff.setProgress(BluPlug.getBluPlugMaxFreqScreenOff());
            maxFreqScreenOff.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugMaxFreqScreenOff(position, getActivity());
                }
            });

            bluplug.addItem(maxFreqScreenOff);
        }

        if (BluPlug.hasBluPlugUpThreshold()) {
            SeekBarView upThreshold = new SeekBarView();
            upThreshold.setTitle(getString(R.string.up_threshold));
            upThreshold.setSummary(getString(R.string.up_threshold_summary));
            upThreshold.setUnit("%");
            upThreshold.setMax(100);
            upThreshold.setProgress(BluPlug.getBluPlugUpThreshold());
            upThreshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugUpThreshold(position, getActivity());
                }
            });

            bluplug.addItem(upThreshold);
        }

        if (BluPlug.hasBluPlugUpTimerCnt()) {
            List<String> list = new ArrayList<>();
            for (float i = 0; i < 21; i++) {
                list.add(String.valueOf(i * 0.5f));
            }

            SeekBarView upTimerCnt = new SeekBarView();
            upTimerCnt.setTitle(getString(R.string.up_timer_cnt));
            upTimerCnt.setSummary(getString(R.string.up_timer_cnt_summary));
            upTimerCnt.setItems(list);
            upTimerCnt.setProgress(BluPlug.getBluPlugUpTimerCnt());
            upTimerCnt.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugUpTimerCnt(position, getActivity());
                }
            });

            bluplug.addItem(upTimerCnt);
        }

        if (BluPlug.hasBluPlugDownTimerCnt()) {
            List<String> list = new ArrayList<>();
            for (float i = 0; i < 21; i++) {
                list.add(String.valueOf(i * 0.5f));
            }

            SeekBarView downTimerCnt = new SeekBarView();
            downTimerCnt.setTitle(getString(R.string.down_timer_cnt));
            downTimerCnt.setSummary(getString(R.string.down_timer_cnt_summary));
            downTimerCnt.setItems(list);
            downTimerCnt.setProgress(BluPlug.getBluPlugDownTimerCnt());
            downTimerCnt.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    BluPlug.setBluPlugDownTimerCnt(position, getActivity());
                }
            });

            bluplug.addItem(downTimerCnt);
        }

        if (bluplug.size() > 0) {
            items.add(bluplug);
        }
    }

    private void dynHotplugInit(List<RecyclerViewItem> items) {
        final CardView dynhotplug = new CardView(getActivity());
        dynhotplug.setTitle(getString(R.string.dyn_hotplug));

        if (DynHotplug.hasDynHotplugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.dyn_hotplug));
            enable.setSummary(getString(R.string.dyn_hotplug_summary));
            enable.setChecked(DynHotplug.isDynHotplugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    DynHotplug.enableDynHotplug(isChecked, getActivity());
                }
            });

            dynhotplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (DynHotplug.hasDynHotplugMinOnline()) {
            SeekBarView minOnline = new SeekBarView();
            minOnline.setTitle(getString(R.string.min_cpu_online));
            minOnline.setSummary(getString(R.string.min_cpu_online_summary));
            minOnline.setMax(CPUFreq.getCpuCount());
            minOnline.setMin(1);
            minOnline.setProgress(DynHotplug.getDynHotplugMinOnline() - 1);
            minOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    DynHotplug.setDynHotplugMinOnline(position + 1, getActivity());
                }
            });

            dynhotplug.addItem(minOnline);
        }

        if (DynHotplug.hasDynHotplugMaxOnline()) {
            SeekBarView maxOnline = new SeekBarView();
            maxOnline.setTitle(getString(R.string.max_cpu_online));
            maxOnline.setSummary(getString(R.string.max_cpu_online_summary));
            maxOnline.setMax(CPUFreq.getCpuCount());
            maxOnline.setMin(1);
            maxOnline.setProgress(DynHotplug.getDynHotplugMaxOnline() - 1);
            maxOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    DynHotplug.setDynHotplugMaxOnline(position + 1, getActivity());
                }
            });

            dynhotplug.addItem(maxOnline);
        }

        if (DynHotplug.hasDynHotplugUpThreshold()) {
            SeekBarView upThreshold = new SeekBarView();
            upThreshold.setTitle(getString(R.string.up_threshold));
            upThreshold.setSummary(getString(R.string.up_threshold_summary));
            upThreshold.setUnit("%");
            upThreshold.setMax(100);
            upThreshold.setProgress(DynHotplug.getDynHotplugUpThreshold());
            upThreshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    DynHotplug.setDynHotplugUpThreshold(position, getActivity());
                }
            });

            dynhotplug.addItem(upThreshold);
        }

        if (DynHotplug.hasDynHotplugUpTimerCnt()) {
            List<String> list = new ArrayList<>();
            for (float i = 0; i < 21; i++) {
                list.add(String.valueOf(i * 0.5f));
            }

            SeekBarView upTimerCnt = new SeekBarView();
            upTimerCnt.setTitle(getString(R.string.up_timer_cnt));
            upTimerCnt.setSummary(getString(R.string.up_timer_cnt_summary));
            upTimerCnt.setItems(list);
            upTimerCnt.setProgress(DynHotplug.getDynHotplugUpTimerCnt());
            upTimerCnt.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    DynHotplug.setDynHotplugUpTimerCnt(position, getActivity());
                }
            });

            dynhotplug.addItem(upTimerCnt);
        }

        if (DynHotplug.hasDynHotplugDownTimerCnt()) {
            List<String> list = new ArrayList<>();
            for (float i = 0; i < 21; i++) {
                list.add(String.valueOf(i * 0.5f));
            }

            SeekBarView downTimerCnt = new SeekBarView();
            downTimerCnt.setTitle(getString(R.string.down_timer_cnt));
            downTimerCnt.setSummary(getString(R.string.down_timer_cnt_summary));
            downTimerCnt.setItems(list);
            downTimerCnt.setProgress(DynHotplug.getDynHotplugDownTimerCnt());
            downTimerCnt.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    DynHotplug.setDynHotplugDownTimerCnt(position, getActivity());
                }
            });

            dynhotplug.addItem(downTimerCnt);
        }

        if (dynhotplug.size() > 0) {
            items.add(dynhotplug);
        }
    }

    private void msmHotplugInit(List<RecyclerViewItem> items) {
        CardView msmHotplug = new CardView(getActivity());
        msmHotplug.setTitle(getString(R.string.msm_hotplug));

        if (MSMHotplug.hasMsmHotplugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.msm_hotplug));
            enable.setSummary(getString(R.string.msm_hotplug_summary));
            enable.setChecked(MSMHotplug.isMsmHotplugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MSMHotplug.enableMsmHotplug(isChecked, getActivity());
                }
            });

            msmHotplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (MSMHotplug.hasMsmHotplugDebugMask()) {
            SwitchView debugMask = new SwitchView();
            debugMask.setTitle(getString(R.string.debug_mask));
            debugMask.setSummary(getString(R.string.debug_mask_summary));
            debugMask.setChecked(MSMHotplug.isMsmHotplugDebugMaskEnabled());
            debugMask.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MSMHotplug.enableMsmHotplugDebugMask(isChecked, getActivity());
                }
            });

            msmHotplug.addItem(debugMask);
        }

        if (MSMHotplug.hasMsmHotplugMinCpusOnline()) {
            SeekBarView minCpusOnline = new SeekBarView();
            minCpusOnline.setTitle(getString(R.string.min_cpu_online));
            minCpusOnline.setSummary(getString(R.string.min_cpu_online_summary));
            minCpusOnline.setMax(CPUFreq.getCpuCount());
            minCpusOnline.setMin(1);
            minCpusOnline.setProgress(MSMHotplug.getMsmHotplugMinCpusOnline() - 1);
            minCpusOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugMinCpusOnline(position + 1, getActivity());
                }
            });

            msmHotplug.addItem(minCpusOnline);
        }

        if (MSMHotplug.hasMsmHotplugMaxCpusOnline()) {
            SeekBarView maxCpusOnline = new SeekBarView();
            maxCpusOnline.setTitle(getString(R.string.max_cpu_online));
            maxCpusOnline.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpusOnline.setMax(CPUFreq.getCpuCount());
            maxCpusOnline.setMin(1);
            maxCpusOnline.setProgress(MSMHotplug.getMsmHotplugMaxCpusOnline() - 1);
            maxCpusOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugMaxCpusOnline(position + 1, getActivity());
                }
            });

            msmHotplug.addItem(maxCpusOnline);
        }

        if (MSMHotplug.hasMsmHotplugCpusBoosted()) {
            SeekBarView cpusBoosted = new SeekBarView();
            cpusBoosted.setTitle(getString(R.string.cpus_boosted));
            cpusBoosted.setSummary(getString(R.string.cpus_boosted_summary));
            cpusBoosted.setMax(CPUFreq.getCpuCount());
            cpusBoosted.setMin(1);
            cpusBoosted.setProgress(MSMHotplug.getMsmHotplugCpusBoosted());
            cpusBoosted.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugCpusBoosted(position, getActivity());
                }
            });

            msmHotplug.addItem(cpusBoosted);
        }

        if (MSMHotplug.hasMsmHotplugMaxCpusOnlineSusp()) {
            SeekBarView maxCpusOnlineSusp = new SeekBarView();
            maxCpusOnlineSusp.setTitle(getString(R.string.max_cpu_online_screen_off));
            maxCpusOnlineSusp.setSummary(getString(R.string.max_cpu_online_screen_off_summary));
            maxCpusOnlineSusp.setMax(CPUFreq.getCpuCount());
            maxCpusOnlineSusp.setMin(1);
            maxCpusOnlineSusp.setProgress(MSMHotplug.getMsmHotplugMaxCpusOnlineSusp() - 1);
            maxCpusOnlineSusp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugMaxCpusOnlineSusp(position + 1, getActivity());
                }
            });

            msmHotplug.addItem(maxCpusOnlineSusp);
        }

        if (MSMHotplug.hasMsmHotplugBoostLockDuration()) {
            SeekBarView boostLockDuration = new SeekBarView();
            boostLockDuration.setTitle(getString(R.string.boost_lock_duration));
            boostLockDuration.setSummary(getString(R.string.boost_lock_duration_summary));
            boostLockDuration.setMax(5000);
            boostLockDuration.setMin(1);
            boostLockDuration.setProgress(MSMHotplug.getMsmHotplugBoostLockDuration() - 1);
            boostLockDuration.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugBoostLockDuration(position + 1, getActivity());
                }
            });

            msmHotplug.addItem(boostLockDuration);
        }

        if (MSMHotplug.hasMsmHotplugDownLockDuration()) {
            SeekBarView downLockDuration = new SeekBarView();
            downLockDuration.setTitle(getString(R.string.down_lock_duration));
            downLockDuration.setSummary(getString(R.string.down_lock_duration_summary));
            downLockDuration.setMax(5000);
            downLockDuration.setMin(1);
            downLockDuration.setProgress(MSMHotplug.getMsmHotplugDownLockDuration() - 1);
            downLockDuration.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugDownLockDuration(position + 1, getActivity());
                }
            });

            msmHotplug.addItem(downLockDuration);
        }

        if (MSMHotplug.hasMsmHotplugHistorySize()) {
            SeekBarView historySize = new SeekBarView();
            historySize.setTitle(getString(R.string.history_size));
            historySize.setSummary(getString(R.string.history_size_summary));
            historySize.setMax(60);
            historySize.setMin(1);
            historySize.setProgress(MSMHotplug.getMsmHotplugHistorySize() - 1);
            historySize.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugHistorySize(position + 1, getActivity());
                }
            });

            msmHotplug.addItem(historySize);
        }

        if (MSMHotplug.hasMsmHotplugUpdateRate()) {
            SeekBarView updateRate = new SeekBarView();
            updateRate.setTitle(getString(R.string.update_rate));
            updateRate.setSummary(getString(R.string.update_rate_summary));
            updateRate.setMax(60);
            updateRate.setProgress(MSMHotplug.getMsmHotplugUpdateRate());
            updateRate.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugUpdateRate(position, getActivity());
                }
            });

            msmHotplug.addItem(updateRate);
        }

        if (MSMHotplug.hasMsmHotplugFastLaneLoad()) {
            SeekBarView fastLaneLoad = new SeekBarView();
            fastLaneLoad.setTitle(getString(R.string.fast_lane_load));
            fastLaneLoad.setSummary(getString(R.string.fast_lane_load_summary));
            fastLaneLoad.setMax(400);
            fastLaneLoad.setProgress(MSMHotplug.getMsmHotplugFastLaneLoad());
            fastLaneLoad.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugFastLaneLoad(position, getActivity());
                }
            });

            msmHotplug.addItem(fastLaneLoad);
        }

        if (MSMHotplug.hasMsmHotplugFastLaneMinFreq() && CPUFreq.getFreqs() != null) {
            SelectView fastLaneMinFreq = new SelectView();
            fastLaneMinFreq.setTitle(getString(R.string.fast_lane_min_freq));
            fastLaneMinFreq.setSummary(getString(R.string.fast_lane_min_freq_summary));
            fastLaneMinFreq.setItems(CPUFreq.getAdjustedFreq(getActivity()));
            fastLaneMinFreq.setItem((MSMHotplug.getMsmHotplugFastLaneMinFreq() / 1000) + getString(R.string.mhz));
            fastLaneMinFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    MSMHotplug.setMsmHotplugFastLaneMinFreq(CPUFreq.getFreqs().get(position), getActivity());
                }
            });

            msmHotplug.addItem(fastLaneMinFreq);
        }

        if (MSMHotplug.hasMsmHotplugOfflineLoad()) {
            SeekBarView offlineLoad = new SeekBarView();
            offlineLoad.setTitle(getString(R.string.offline_load));
            offlineLoad.setSummary(getString(R.string.offline_load_summary));
            offlineLoad.setProgress(MSMHotplug.getMsmHotplugOfflineLoad());
            offlineLoad.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugOfflineLoad(position, getActivity());
                }
            });

            msmHotplug.addItem(offlineLoad);
        }

        if (MSMHotplug.hasMsmHotplugIoIsBusy()) {
            SwitchView ioIsBusy = new SwitchView();
            ioIsBusy.setTitle(getString(R.string.io_is_busy));
            ioIsBusy.setSummary(getString(R.string.io_is_busy_summary));
            ioIsBusy.setChecked(MSMHotplug.isMsmHotplugIoIsBusyEnabled());
            ioIsBusy.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MSMHotplug.enableMsmHotplugIoIsBusy(isChecked, getActivity());
                }
            });

            msmHotplug.addItem(ioIsBusy);
        }

        if (MSMHotplug.hasMsmHotplugSuspendMaxCpus()) {
            SeekBarView suspendMaxCpus = new SeekBarView();
            suspendMaxCpus.setTitle(getString(R.string.max_cpu_online_screen_off));
            suspendMaxCpus.setSummary(getString(R.string.max_cpu_online_screen_off_summary));
            suspendMaxCpus.setMax(CPUFreq.getCpuCount());
            suspendMaxCpus.setMin(1);
            suspendMaxCpus.setProgress(MSMHotplug.getMsmHotplugSuspendMaxCpus());
            suspendMaxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugSuspendMaxCpus(position, getActivity());
                }
            });

            msmHotplug.addItem(suspendMaxCpus);
        }

        if (MSMHotplug.hasMsmHotplugSuspendFreq() && CPUFreq.getFreqs() != null) {
            SelectView suspendFreq = new SelectView();
            suspendFreq.setTitle(getString(R.string.cpu_max_screen_off_freq));
            suspendFreq.setSummary(getString(R.string.cpu_max_screen_off_freq_summary));
            suspendFreq.setItems(CPUFreq.getAdjustedFreq(getActivity()));
            suspendFreq.setItem((MSMHotplug.getMsmHotplugSuspendFreq() / 1000) + getString(R.string.mhz));
            suspendFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    MSMHotplug.setMsmHotplugSuspendFreq(CPUFreq.getFreqs().get(position), getActivity());
                }
            });

            msmHotplug.addItem(suspendFreq);
        }

        if (MSMHotplug.hasMsmHotplugSuspendDeferTime()) {
            SeekBarView suspendDeferTime = new SeekBarView();
            suspendDeferTime.setTitle(getString(R.string.suspend_defer_time));
            suspendDeferTime.setUnit(getString(R.string.ms));
            suspendDeferTime.setMax(5000);
            suspendDeferTime.setOffset(10);
            suspendDeferTime.setProgress(MSMHotplug.getMsmHotplugSuspendDeferTime() / 10);
            suspendDeferTime.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MSMHotplug.setMsmHotplugSuspendDeferTime(position * 10, getActivity());
                }
            });

            msmHotplug.addItem(suspendDeferTime);
        }

        if (msmHotplug.size() > 0) {
            items.add(msmHotplug);
        }
    }

    private void makoHotplugInit(List<RecyclerViewItem> items) {
        CardView makoHotplug = new CardView(getActivity());
        makoHotplug.setTitle(getString(R.string.mako_hotplug));

        if (MakoHotplug.hasMakoHotplugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.mako_hotplug));
            enable.setSummary(getString(R.string.mako_hotplug_summary));
            enable.setChecked(MakoHotplug.isMakoHotplugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MakoHotplug.enableMakoHotplug(isChecked, getActivity());
                }
            });

            makoHotplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (MakoHotplug.hasMakoHotplugCoresOnTouch()) {
            SeekBarView coresOnTouch = new SeekBarView();
            coresOnTouch.setTitle(getString(R.string.cpus_on_touch));
            coresOnTouch.setSummary(getString(R.string.cpus_on_touch_summary));
            coresOnTouch.setMax(CPUFreq.getCpuCount());
            coresOnTouch.setMin(1);
            coresOnTouch.setProgress(MakoHotplug.getMakoHotplugCoresOnTouch() - 1);
            coresOnTouch.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugCoresOnTouch(position + 1, getActivity());
                }
            });

            makoHotplug.addItem(coresOnTouch);
        }

        if (MakoHotplug.hasMakoHotplugCpuFreqUnplugLimit() && CPUFreq.getFreqs() != null) {
            SelectView cpufreqUnplugLimit = new SelectView();
            cpufreqUnplugLimit.setSummary(getString(R.string.cpu_freq_unplug_limit));
            cpufreqUnplugLimit.setItems(CPUFreq.getAdjustedFreq(getActivity()));
            cpufreqUnplugLimit.setItem((MakoHotplug.getMakoHotplugCpuFreqUnplugLimit() / 1000)
                    + getString(R.string.mhz));
            cpufreqUnplugLimit.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    MakoHotplug.setMakoHotplugCpuFreqUnplugLimit(CPUFreq.getFreqs().get(position), getActivity());
                }
            });

            makoHotplug.addItem(cpufreqUnplugLimit);
        }

        if (MakoHotplug.hasMakoHotplugFirstLevel()) {
            SeekBarView firstLevel = new SeekBarView();
            firstLevel.setTitle(getString(R.string.first_level));
            firstLevel.setSummary(getString(R.string.first_level_summary));
            firstLevel.setUnit("%");
            firstLevel.setProgress(MakoHotplug.getMakoHotplugFirstLevel());
            firstLevel.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugFirstLevel(position, getActivity());
                }
            });

            makoHotplug.addItem(firstLevel);
        }

        if (MakoHotplug.hasMakoHotplugHighLoadCounter()) {
            SeekBarView highLoadCounter = new SeekBarView();
            highLoadCounter.setTitle(getString(R.string.high_load_counter));
            highLoadCounter.setProgress(MakoHotplug.getMakoHotplugHighLoadCounter());
            highLoadCounter.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugHighLoadCounter(position, getActivity());
                }
            });

            makoHotplug.addItem(highLoadCounter);
        }

        if (MakoHotplug.hasMakoHotplugLoadThreshold()) {
            SeekBarView loadThreshold = new SeekBarView();
            loadThreshold.setTitle(getString(R.string.load_threshold));
            loadThreshold.setSummary(getString(R.string.load_threshold_summary));
            loadThreshold.setUnit("%");
            loadThreshold.setProgress(MakoHotplug.getMakoHotplugLoadThreshold());
            loadThreshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugLoadThreshold(position, getActivity());
                }
            });

            makoHotplug.addItem(loadThreshold);
        }

        if (MakoHotplug.hasMakoHotplugMaxLoadCounter()) {
            SeekBarView maxLoadCounter = new SeekBarView();
            maxLoadCounter.setTitle(getString(R.string.max_load_counter));
            maxLoadCounter.setProgress(MakoHotplug.getMakoHotplugMaxLoadCounter());
            maxLoadCounter.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugMaxLoadCounter(position, getActivity());
                }
            });

            makoHotplug.addItem(maxLoadCounter);
        }

        if (MakoHotplug.hasMakoHotplugMinTimeCpuOnline()) {
            SeekBarView minTimeCpuOnline = new SeekBarView();
            minTimeCpuOnline.setTitle(getString(R.string.min_time_cpu_online));
            minTimeCpuOnline.setProgress(MakoHotplug.getMakoHotplugMinTimeCpuOnline());
            minTimeCpuOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugMinTimeCpuOnline(position, getActivity());
                }
            });

            makoHotplug.addItem(minTimeCpuOnline);
        }

        if (MakoHotplug.hasMakoHotplugMinCoresOnline()) {
            SeekBarView minCoresOnline = new SeekBarView();
            minCoresOnline.setTitle(getString(R.string.min_cpu_online));
            minCoresOnline.setSummary(getString(R.string.min_cpu_online_summary));
            minCoresOnline.setMax(CPUFreq.getCpuCount());
            minCoresOnline.setMin(1);
            minCoresOnline.setProgress(MakoHotplug.getMakoHotplugMinCoresOnline() - 1);
            minCoresOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugMinCoresOnline(position + 1, getActivity());
                }
            });

            makoHotplug.addItem(minCoresOnline);
        }

        if (MakoHotplug.hasMakoHotplugTimer()) {
            SeekBarView timer = new SeekBarView();
            timer.setTitle(getString(R.string.timer));
            timer.setProgress(MakoHotplug.getMakoHotplugTimer());
            timer.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MakoHotplug.setMakoHotplugTimer(position, getActivity());
                }
            });

            makoHotplug.addItem(timer);
        }

        if (MakoHotplug.hasMakoHotplugSuspendFreq() && CPUFreq.getFreqs() != null) {
            SelectView suspendFreq = new SelectView();
            suspendFreq.setTitle(getString(R.string.cpu_max_screen_off_freq));
            suspendFreq.setSummary(getString(R.string.cpu_max_screen_off_freq_summary));
            suspendFreq.setItems(CPUFreq.getAdjustedFreq(getActivity()));
            suspendFreq.setItem((MakoHotplug.getMakoHotplugSuspendFreq() / 1000) + getString(R.string.mhz));
            suspendFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    MakoHotplug.setMakoHotplugSuspendFreq(CPUFreq.getFreqs().get(position), getActivity());
                }
            });

            makoHotplug.addItem(suspendFreq);
        }

        if (makoHotplug.size() > 0) {
            items.add(makoHotplug);
        }
    }

    private void mbHotplugInit(List<RecyclerViewItem> items) {
        CardView mbHotplug = new CardView(getActivity());
        mbHotplug.setTitle(getString(R.string.bricked_hotplug));

        if (MBHotplug.hasMBGHotplugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(MBHotplug.getMBName(getActivity()));
            enable.setSummary(getString(R.string.mb_hotplug_summary));
            enable.setChecked(MBHotplug.isMBHotplugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MBHotplug.enableMBHotplug(isChecked, getActivity());
                }
            });

            mbHotplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (MBHotplug.hasMBHotplugScroffSingleCore()) {
            SwitchView scroffSingleCore = new SwitchView();
            scroffSingleCore.setTitle(getString(R.string.screen_off_single_cpu));
            scroffSingleCore.setSummary(getString(R.string.screen_off_single_cpu_summary));
            scroffSingleCore.setChecked(MBHotplug.isMBHotplugScroffSingleCoreEnabled());
            scroffSingleCore.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MBHotplug.enableMBHotplugScroffSingleCore(isChecked, getActivity());
                }
            });

            mbHotplug.addItem(scroffSingleCore);
        }

        if (MBHotplug.hasMBHotplugMinCpus()) {
            SeekBarView minCpus = new SeekBarView();
            minCpus.setTitle(getString(R.string.min_cpu_online));
            minCpus.setSummary(getString(R.string.min_cpu_online_summary));
            minCpus.setMax(CPUFreq.getCpuCount());
            minCpus.setMin(1);
            minCpus.setProgress(MBHotplug.getMBHotplugMinCpus() - 1);
            minCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugMinCpus(position + 1, getActivity());
                }
            });

            mbHotplug.addItem(minCpus);
        }

        if (MBHotplug.hasMBHotplugMaxCpus()) {
            SeekBarView maxCpus = new SeekBarView();
            maxCpus.setTitle(getString(R.string.max_cpu_online));
            maxCpus.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpus.setMax(CPUFreq.getCpuCount());
            maxCpus.setMin(1);
            maxCpus.setProgress(MBHotplug.getMBHotplugMaxCpus() - 1);
            maxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugMaxCpus(position + 1, getActivity());
                }
            });

            mbHotplug.addItem(maxCpus);
        }

        if (MBHotplug.hasMBHotplugMaxCpusOnlineSusp()) {
            SeekBarView maxCpusOnlineSusp = new SeekBarView();
            maxCpusOnlineSusp.setTitle(getString(R.string.max_cpu_online_screen_off));
            maxCpusOnlineSusp.setSummary(getString(R.string.max_cpu_online_screen_off_summary));
            maxCpusOnlineSusp.setMax(CPUFreq.getCpuCount());
            maxCpusOnlineSusp.setMin(1);
            maxCpusOnlineSusp.setProgress(MBHotplug.getMBHotplugMaxCpusOnlineSusp() - 1);
            maxCpusOnlineSusp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugMaxCpusOnlineSusp(position + 1, getActivity());
                }
            });

            mbHotplug.addItem(maxCpusOnlineSusp);
        }

        if (MBHotplug.hasMBHotplugIdleFreq() && CPUFreq.getFreqs() != null) {
            SelectView idleFreq = new SelectView();
            idleFreq.setTitle(getString(R.string.idle_frequency));
            idleFreq.setSummary(getString(R.string.idle_frequency_summary));
            idleFreq.setItems(CPUFreq.getAdjustedFreq(getActivity()));
            idleFreq.setItem((MBHotplug.getMBHotplugIdleFreq() / 1000) + getString(R.string.mhz));
            idleFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    MBHotplug.setMBHotplugIdleFreq(CPUFreq.getFreqs().get(position), getActivity());
                }
            });

            mbHotplug.addItem(idleFreq);
        }

        if (MBHotplug.hasMBHotplugBoostEnable()) {
            SwitchView boost = new SwitchView();
            boost.setTitle(getString(R.string.touch_boost));
            boost.setSummary(getString(R.string.touch_boost_summary));
            boost.setChecked(MBHotplug.isMBHotplugBoostEnabled());
            boost.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    MBHotplug.enableMBHotplugBoost(isChecked, getActivity());
                }
            });

            mbHotplug.addItem(boost);
        }

        if (MBHotplug.hasMBHotplugBoostTime()) {
            SeekBarView boostTime = new SeekBarView();
            boostTime.setTitle(getString(R.string.touch_boost_time));
            boostTime.setSummary(getString(R.string.touch_boost_time_summary));
            boostTime.setUnit(getString(R.string.ms));
            boostTime.setMax(5000);
            boostTime.setOffset(100);
            boostTime.setProgress(MBHotplug.getMBHotplugBoostTime() / 100);
            boostTime.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugBoostTime(position * 100, getActivity());
                }
            });

            mbHotplug.addItem(boostTime);
        }

        if (MBHotplug.hasMBHotplugCpusBoosted()) {
            SeekBarView cpusBoosted = new SeekBarView();
            cpusBoosted.setTitle(getString(R.string.cpus_boosted));
            cpusBoosted.setSummary(getString(R.string.cpus_boosted_summary));
            cpusBoosted.setMax(CPUFreq.getCpuCount());
            cpusBoosted.setMin(1);
            cpusBoosted.setProgress(MBHotplug.getMBHotplugCpusBoosted());
            cpusBoosted.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugCpusBoosted(position, getActivity());
                }
            });

            mbHotplug.addItem(cpusBoosted);
        }

        if (MBHotplug.hasMBHotplugBoostFreqs() && CPUFreq.getFreqs() != null) {
            List<Integer> freqs = MBHotplug.getMBHotplugBoostFreqs();

            for (int i = 0; i < freqs.size(); i++) {
                SelectView boostFreq = new SelectView();
                boostFreq.setSummary(getString(R.string.boost_frequency_core, i));
                boostFreq.setItems(CPUFreq.getAdjustedFreq(getActivity()));
                boostFreq.setItem((freqs.get(i) / 1000) + getString(R.string.mhz));
                final int pos = i;
                boostFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                    @Override
                    public void onItemSelected(SelectView selectView, int position, String item) {
                        MBHotplug.setMBHotplugBoostFreqs(pos, CPUFreq.getFreqs().get(position), getActivity());
                    }
                });

                mbHotplug.addItem(boostFreq);
            }
        }

        if (MBHotplug.hasMBHotplugStartDelay()) {
            SeekBarView startDelay = new SeekBarView();
            startDelay.setTitle(getString(R.string.start_delay));
            startDelay.setSummary(getString(R.string.start_delay_summary));
            startDelay.setUnit(getString(R.string.ms));
            startDelay.setMax(5000);
            startDelay.setOffset(1000);
            startDelay.setProgress(MBHotplug.getMBHotplugStartDelay() / 1000);
            startDelay.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugStartDelay(position * 1000, getActivity());
                }
            });

            mbHotplug.addItem(startDelay);
        }

        if (MBHotplug.hasMBHotplugDelay()) {
            SeekBarView delay = new SeekBarView();
            delay.setTitle(getString(R.string.delay));
            delay.setSummary(getString(R.string.delay_summary));
            delay.setUnit(getString(R.string.ms));
            delay.setMax(200);
            delay.setProgress(MBHotplug.getMBHotplugDelay());
            delay.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugDelay(position, getActivity());
                }
            });

            mbHotplug.addItem(delay);
        }

        if (MBHotplug.hasMBHotplugPause()) {
            SeekBarView pause = new SeekBarView();
            pause.setTitle(getString(R.string.pause));
            pause.setSummary(getString(R.string.pause_summary));
            pause.setUnit(getString(R.string.ms));
            pause.setMax(5000);
            pause.setOffset(1000);
            pause.setProgress(MBHotplug.getMBHotplugPause() / 1000);
            pause.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    MBHotplug.setMBHotplugPause(position * 1000, getActivity());
                }
            });

            mbHotplug.addItem(pause);
        }

        if (mbHotplug.size() > 0) {
            items.add(mbHotplug);
        }
    }

    private void alucardHotplugInit(List<RecyclerViewItem> items) {
        CardView alucardHotplug = new CardView(getActivity());
        alucardHotplug.setTitle(getString(R.string.alucard_hotplug));

        if (AlucardHotplug.hasAlucardHotplugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.alucard_hotplug));
            enable.setSummary(getString(R.string.alucard_hotplug_summary));
            enable.setChecked(AlucardHotplug.isAlucardHotplugEnable());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AlucardHotplug.enableAlucardHotplug(isChecked, getActivity());
                }
            });

            alucardHotplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (AlucardHotplug.hasAlucardHotplugHpIoIsBusy()) {
            SwitchView ioIsBusy = new SwitchView();
            ioIsBusy.setTitle(getString(R.string.io_is_busy));
            ioIsBusy.setSummary(getString(R.string.io_is_busy_summary));
            ioIsBusy.setChecked(AlucardHotplug.isAlucardHotplugHpIoIsBusyEnable());
            ioIsBusy.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AlucardHotplug.enableAlucardHotplugHpIoIsBusy(isChecked, getActivity());
                }
            });

            alucardHotplug.addItem(ioIsBusy);
        }

        if (AlucardHotplug.hasAlucardHotplugSamplingRate()) {
            SeekBarView samplingRate = new SeekBarView();
            samplingRate.setTitle(getString(R.string.sampling_rate));
            samplingRate.setUnit("%");
            samplingRate.setMin(1);
            samplingRate.setProgress(AlucardHotplug.getAlucardHotplugSamplingRate() - 1);
            samplingRate.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AlucardHotplug.setAlucardHotplugSamplingRate(position + 1, getActivity());
                }
            });

            alucardHotplug.addItem(samplingRate);
        }

        if (AlucardHotplug.hasAlucardHotplugSuspend()) {
            SwitchView suspend = new SwitchView();
            suspend.setTitle(getString(R.string.suspend));
            suspend.setSummary(getString(R.string.suspend_summary));
            suspend.setChecked(AlucardHotplug.isAlucardHotplugSuspendEnabled());
            suspend.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AlucardHotplug.enableAlucardHotplugSuspend(isChecked, getActivity());
                }
            });

            alucardHotplug.addItem(suspend);
        }

        if (AlucardHotplug.hasAlucardHotplugMinCpusOnline()) {
            SeekBarView minCpusOnline = new SeekBarView();
            minCpusOnline.setTitle(getString(R.string.min_cpu_online));
            minCpusOnline.setSummary(getString(R.string.min_cpu_online_summary));
            minCpusOnline.setMax(CPUFreq.getCpuCount());
            minCpusOnline.setMin(1);
            minCpusOnline.setProgress(AlucardHotplug.getAlucardHotplugMinCpusOnline() - 1);
            minCpusOnline.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AlucardHotplug.setAlucardHotplugMinCpusOnline(position + 1, getActivity());
                }
            });

            alucardHotplug.addItem(minCpusOnline);
        }

        if (AlucardHotplug.hasAlucardHotplugMaxCoresLimit()) {
            SeekBarView maxCoresLimit = new SeekBarView();
            maxCoresLimit.setTitle(getString(R.string.max_cpu_online));
            maxCoresLimit.setSummary(getString(R.string.max_cpu_online_summary));
            maxCoresLimit.setMax(CPUFreq.getCpuCount());
            maxCoresLimit.setMin(1);
            maxCoresLimit.setProgress(AlucardHotplug.getAlucardHotplugMaxCoresLimit() - 1);
            maxCoresLimit.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AlucardHotplug.setAlucardHotplugMaxCoresLimit(position + 1, getActivity());
                }
            });

            alucardHotplug.addItem(maxCoresLimit);
        }

        if (AlucardHotplug.hasAlucardHotplugMaxCoresLimitSleep()) {
            SeekBarView maxCoresLimitSleep = new SeekBarView();
            maxCoresLimitSleep.setTitle(getString(R.string.max_cpu_online_screen_off));
            maxCoresLimitSleep.setSummary(getString(R.string.max_cpu_online_screen_off_summary));
            maxCoresLimitSleep.setMax(CPUFreq.getCpuCount());
            maxCoresLimitSleep.setMin(1);
            maxCoresLimitSleep.setProgress(AlucardHotplug.getAlucardHotplugMaxCoresLimitSleep() - 1);
            maxCoresLimitSleep.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AlucardHotplug.setAlucardHotplugMaxCoresLimitSleep(position + 1, getActivity());
                }
            });

            alucardHotplug.addItem(maxCoresLimitSleep);
        }

        if (AlucardHotplug.hasAlucardHotplugCpuDownRate()) {
            SeekBarView cpuDownRate = new SeekBarView();
            cpuDownRate.setTitle(getString(R.string.cpu_down_rate));
            cpuDownRate.setUnit("%");
            cpuDownRate.setMin(1);
            cpuDownRate.setProgress(AlucardHotplug.getAlucardHotplugCpuDownRate() - 1);
            cpuDownRate.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AlucardHotplug.setAlucardHotplugCpuDownRate(position + 1, getActivity());
                }
            });

            alucardHotplug.addItem(cpuDownRate);
        }

        if (AlucardHotplug.hasAlucardHotplugCpuUpRate()) {
            SeekBarView cpuUpRate = new SeekBarView();
            cpuUpRate.setTitle(getString(R.string.cpu_up_rate));
            cpuUpRate.setUnit("%");
            cpuUpRate.setMin(1);
            cpuUpRate.setProgress(AlucardHotplug.getAlucardHotplugCpuUpRate() - 1);
            cpuUpRate.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AlucardHotplug.setAlucardHotplugCpuUpRate(position + 1, getActivity());
                }
            });

            alucardHotplug.addItem(cpuUpRate);
        }

        if (alucardHotplug.size() > 0) {
            items.add(alucardHotplug);
        }
    }

    private void skaterHotplugInit(List<RecyclerViewItem> items) {
        CardView skaterHotplug = new CardView(getActivity());
        skaterHotplug.setTitle(getString(R.string.skater_hotplug));

        if (SkaterHotplug.hasSkaterHotplugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.skater_hotplug));
            enable.setSummary(getString(R.string.skater_hotplug_summary));
            enable.setChecked(SkaterHotplug.isSkaterHotplugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    SkaterHotplug.enableSkaterHotplug(isChecked, getActivity());
                }
            });

            skaterHotplug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (SkaterHotplug.hasSkaterHotplugDelay()) {
            SeekBarView delay = new SeekBarView();
            delay.setTitle(getString(R.string.delay));
            delay.setSummary(getString(R.string.delay_summary));
            delay.setUnit(getString(R.string.ms));
            delay.setMax(500);
            delay.setProgress(SkaterHotplug.getSkaterHotplugDelay());
            delay.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugDelay(position, getActivity());
                }
            });

            skaterHotplug.addItem(delay);
        }

        if (SkaterHotplug.hasSkaterHotplugMaxCpus()) {
            SeekBarView maxCpus = new SeekBarView();
            maxCpus.setTitle(getString(R.string.max_cpu_online));
            maxCpus.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpus.setMax(CPUFreq.getCpuCount());
            maxCpus.setMin(1);
            maxCpus.setProgress(SkaterHotplug.getSkaterHotplugMaxCpus() - 1);
            maxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugMaxCpus(position + 1, getActivity());
                }
            });

            skaterHotplug.addItem(maxCpus);
        }

        if (SkaterHotplug.hasSkaterHotplugMinCpus()) {
            SeekBarView minCpus = new SeekBarView();
            minCpus.setTitle(getString(R.string.min_cpu_online));
            minCpus.setSummary(getString(R.string.min_cpu_online_summary));
            minCpus.setMax(CPUFreq.getCpuCount());
            minCpus.setMin(1);
            minCpus.setProgress(SkaterHotplug.getSkaterHotplugMinCpus() - 1);
            minCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugMinCpus(position + 1, getActivity());
                }
            });

            skaterHotplug.addItem(minCpus);
        }

        if (SkaterHotplug.hasSkaterHotplugMaxCpusScroff()) {
            SwitchView maxCpusScroff = new SwitchView();
            maxCpusScroff.setTitle(getString(R.string.screen_off_single_cpu));
            maxCpusScroff.setSummary(getString(R.string.screen_off_single_cpu_summary));
            maxCpusScroff.setChecked(SkaterHotplug.isSkaterHotplugMaxCpusScroff());
            maxCpusScroff.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    SkaterHotplug.maxCpusScroffSkaterHotplug(isChecked, getActivity());
                }
            });

            skaterHotplug.addItem(maxCpusScroff);
        }

        if (SkaterHotplug.hasSkaterHotplugCpufreqDown()) {
            SeekBarView cpuFreqDown = new SeekBarView();
            cpuFreqDown.setTitle(getString(R.string.downrate_limits));
            cpuFreqDown.setUnit("%");
            cpuFreqDown.setProgress(SkaterHotplug.getSkaterHotplugCpufreqDown());
            cpuFreqDown.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugCpufreqDown(position, getActivity());
                }
            });

            skaterHotplug.addItem(cpuFreqDown);
        }

        if (SkaterHotplug.hasSkaterHotplugCpufreqUp()) {
            SeekBarView cpuFreqUp = new SeekBarView();
            cpuFreqUp.setTitle(getString(R.string.uprate_limits));
            cpuFreqUp.setUnit("%");
            cpuFreqUp.setProgress(SkaterHotplug.getSkaterHotplugCpufreqUp());
            cpuFreqUp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugCpufreqUp(position, getActivity());
                }
            });

            skaterHotplug.addItem(cpuFreqUp);
        }

        if (SkaterHotplug.hasSkaterHotplugCycleDown()) {
            SeekBarView cycleDown = new SeekBarView();
            cycleDown.setTitle(getString(R.string.cycle_down));
            cycleDown.setSummary(getString(R.string.cycle_down_summary));
            cycleDown.setMax(CPUFreq.getCpuCount());
            cycleDown.setProgress(SkaterHotplug.getSkaterHotplugCycleDown());
            cycleDown.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugCycleDown(position, getActivity());
                }
            });

            skaterHotplug.addItem(cycleDown);
        }

        if (SkaterHotplug.hasSkaterHotplugCycleUp()) {
            SeekBarView cycleUp = new SeekBarView();
            cycleUp.setTitle(getString(R.string.cycle_up));
            cycleUp.setSummary(getString(R.string.cycle_up_summary));
            cycleUp.setMax(CPUFreq.getCpuCount());
            cycleUp.setProgress(SkaterHotplug.getSkaterHotplugCycleUp());
            cycleUp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugCycleUp(position, getActivity());
                }
            });

            skaterHotplug.addItem(cycleUp);
        }

        if (SkaterHotplug.hasSkaterHotplugMinBoostFreq() && CPUFreq.getFreqs() != null) {
            SelectView minBoostFreq = new SelectView();
            minBoostFreq.setTitle(getString(R.string.min_boost_freq));
            minBoostFreq.setSummary(getString(R.string.min_boost_freq_summary));
            minBoostFreq.setItems(CPUFreq.getAdjustedFreq(getActivity()));
            minBoostFreq.setItem((SkaterHotplug.getSkaterHotplugMinBoostFreq() / 1000)
                    + getString(R.string.mhz));
            minBoostFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    SkaterHotplug.setSkaterHotplugMinBoostFreq(CPUFreq.getFreqs().get(position), getActivity());
                }
            });

            skaterHotplug.addItem(minBoostFreq);
        }

        if (SkaterHotplug.hasSkaterHotplugBoostLockDuration()) {
            SeekBarView boostLockDuration = new SeekBarView();
            boostLockDuration.setTitle(getString(R.string.boost_lock_duration));
            boostLockDuration.setSummary(getString(R.string.boost_lock_duration_summary));
            boostLockDuration.setUnit(getString(R.string.ms));
            boostLockDuration.setMax(5000);
            boostLockDuration.setMin(1);
            boostLockDuration.setProgress(SkaterHotplug.getSkaterHotplugBoostLockDuration() - 1);
            boostLockDuration.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugBoostLockDuration(position + 1, getActivity());
                }
            });

            skaterHotplug.addItem(boostLockDuration);
        }

        if (SkaterHotplug.hasSkaterHotplugCpusBoosted()) {
            SeekBarView cpusBoosted = new SeekBarView();
            cpusBoosted.setTitle(getString(R.string.cpus_boosted));
            cpusBoosted.setSummary(getString(R.string.cpus_boosted_summary));
            cpusBoosted.setMax(CPUFreq.getCpuCount());
            cpusBoosted.setMin(1);
            cpusBoosted.setProgress(SkaterHotplug.getSkaterHotplugCpusBoosted() - 1);
            cpusBoosted.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SkaterHotplug.setSkaterHotplugCpusBoosted(position + 1, getActivity());
                }
            });

            skaterHotplug.addItem(cpusBoosted);
        }

        if (skaterHotplug.size() > 0) {
            items.add(skaterHotplug);
        }
    }

    private void thunderPlugInit(List<RecyclerViewItem> items) {
        CardView thunderPlug = new CardView(getActivity());
        if (ThunderPlug.hasThunderPlugVersion()){
            thunderPlug.setTitle(ThunderPlug.getThunderPlugVersion());
        }else {
            thunderPlug.setTitle(getString(R.string.thunderplug));
        }

        if (ThunderPlug.hasThunderPlugEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.thunderplug));
            enable.setSummary(getString(R.string.thunderplug_summary));
            enable.setChecked(ThunderPlug.isThunderPlugEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    ThunderPlug.enableThunderPlug(isChecked, getActivity());
                }
            });

            thunderPlug.addItem(enable);
            mEnableViews.add(enable);
        }

        if (ThunderPlug.hasThunderPlugMaxCoreOnline()) {
            SeekBarView maxCpus = new SeekBarView();
            maxCpus.setTitle(getString(R.string.max_cpu_online));
            maxCpus.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpus.setMax(CPUFreq.getCpuCount());
            maxCpus.setMin(1);
            maxCpus.setProgress(ThunderPlug.getThunderPlugMaxCoreOnline() - 1);
            maxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugMaxCoreOnline(position + 1, getActivity());
                }
            });

            thunderPlug.addItem(maxCpus);
        }

        if (ThunderPlug.hasThunderPlugMinCoreOnline()) {
            SeekBarView minCpus = new SeekBarView();
            minCpus.setTitle(getString(R.string.min_cpu_online));
            minCpus.setSummary(getString(R.string.min_cpu_online_summary));
            minCpus.setMax(CPUFreq.getCpuCount());
            minCpus.setMin(1);
            minCpus.setProgress(ThunderPlug.getThunderPlugMinCoreOnline() - 1);
            minCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugMinCoreOnline(position + 1, getActivity());
                }
            });

            thunderPlug.addItem(minCpus);
        }

        if (ThunderPlug.hasThunderPlugSuspendCpus()) {
            SeekBarView suspendCpus = new SeekBarView();
            suspendCpus.setTitle(getString(R.string.min_cpu_online_screen_off));
            suspendCpus.setSummary(getString(R.string.min_cpu_online_screen_off_summary));
            suspendCpus.setMax(CPUFreq.getCpuCount());
            suspendCpus.setMin(1);
            suspendCpus.setProgress(ThunderPlug.getThunderPlugSuspendCpus() - 1);
            suspendCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugSuspendCpus(position + 1, getActivity());
                }
            });

            thunderPlug.addItem(suspendCpus);
        }

        if (ThunderPlug.hasThunderPlugEnduranceLevel()) {
            SelectView enduranceLevel = new SelectView();
            enduranceLevel.setTitle(getString(R.string.endurance_level));
            enduranceLevel.setSummary(getString(R.string.endurance_level_summary));
            enduranceLevel.setItems(Arrays.asList(getResources().getStringArray(R.array.endurance_level_items)));
            enduranceLevel.setItem(ThunderPlug.getThunderPlugEnduranceLevel());
            enduranceLevel.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    ThunderPlug.setThunderPlugEnduranceLevel(position, getActivity());
                }
            });

            thunderPlug.addItem(enduranceLevel);
        }

        if (ThunderPlug.hasThunderPlugSuspend()) {
            SwitchView suspend = new SwitchView();
            suspend.setTitle(getString(R.string.suspend));
            suspend.setSummary(getString(R.string.suspend_summary));
            suspend.setChecked(ThunderPlug.isThunderPlugSuspendEnabled());
            suspend.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    ThunderPlug.enableThunderPlugSuspend(isChecked, getActivity());
                }
            });

            thunderPlug.addItem(suspend);
        }

        if (ThunderPlug.hasThunderPlugSamplingRate()) {
            SeekBarView samplingRate = new SeekBarView();
            samplingRate.setTitle(getString(R.string.sampling_rate));
            samplingRate.setMax(500);
            samplingRate.setMin(10);
            samplingRate.setOffset(10);
            samplingRate.setProgress(ThunderPlug.getThunderPlugSamplingRate() / 10 - 1);
            samplingRate.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugSamplingRate((position + 1) * 10, getActivity());
                }
            });

            thunderPlug.addItem(samplingRate);
        }

        if (ThunderPlug.hasThunderPlugLoadThreshold()) {
            SeekBarView loadThreadshold = new SeekBarView();
            loadThreadshold.setTitle(getString(R.string.load_threshold));
            loadThreadshold.setSummary(getString(R.string.load_threshold_summary));
            loadThreadshold.setMin(11);
            loadThreadshold.setProgress(ThunderPlug.getThunderPlugLoadThreshold() - 11);
            loadThreadshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugLoadThreshold(position + 11, getActivity());
                }
            });

            thunderPlug.addItem(loadThreadshold);
        }

        if (ThunderPlug.hasThunderPlugBoostLockDuration()) {
            SeekBarView boostLockDuration = new SeekBarView();
            boostLockDuration.setTitle(getString(R.string.boost_lock_duration));
            boostLockDuration.setSummary(getString(R.string.boost_lock_duration_summary));
            boostLockDuration.setUnit(getString(R.string.ms));
            boostLockDuration.setMax(5000);
            boostLockDuration.setMin(1);
            boostLockDuration.setProgress(ThunderPlug.getThunderPlugBoostLockDuration() - 1);
            boostLockDuration.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugBoostLockDuration(position + 1, getActivity());
                }
            });

            thunderPlug.addItem(boostLockDuration);
        }

        if (ThunderPlug.hasThunderPlugTouchBoost()) {
            SwitchView touchBoost = new SwitchView();
            touchBoost.setTitle(getString(R.string.touch_boost));
            touchBoost.setSummary(getString(R.string.touch_boost_summary));
            touchBoost.setChecked(ThunderPlug.isThunderPlugTouchBoostEnabled());
            touchBoost.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    ThunderPlug.enableThunderPlugTouchBoost(isChecked, getActivity());
                }
            });

            thunderPlug.addItem(touchBoost);
        }

        if (ThunderPlug.hasThunderPlugCpusBoosted()) {
            SeekBarView cpusBoosted = new SeekBarView();
            cpusBoosted.setTitle(getString(R.string.cpus_boosted));
            cpusBoosted.setSummary(getString(R.string.cpus_boosted_summary));
            cpusBoosted.setMax(CPUFreq.getCpuCount());
            cpusBoosted.setMin(1);
            cpusBoosted.setProgress(ThunderPlug.getThunderPlugCpusBoosted() - 1);
            cpusBoosted.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ThunderPlug.setThunderPlugLoadCpusBoosted(position + 1, getActivity());
                }
            });

            thunderPlug.addItem(cpusBoosted);
        }

        if (thunderPlug.size() > 0) {
            items.add(thunderPlug);
        }
    }

    private void zenDecisionInit(List<RecyclerViewItem> items) {
        CardView zenDecision = new CardView(getActivity());
        zenDecision.setTitle(getString(R.string.zen_decision));

        if (ZenDecision.hasZenDecisionEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.zen_decision));
            enable.setSummary(getString(R.string.zen_decision_summary));
            enable.setChecked(ZenDecision.isZenDecisionEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    ZenDecision.enableZenDecision(isChecked, getActivity());
                }
            });

            zenDecision.addItem(enable);
            mEnableViews.add(enable);
        }

        if (ZenDecision.hasZenDecisionWakeWaitTime()) {
            SeekBarView wakeWaitTime = new SeekBarView();
            wakeWaitTime.setTitle(getString(R.string.wake_wait_time));
            wakeWaitTime.setSummary(getString(R.string.wake_wait_time_summary));
            wakeWaitTime.setUnit(getString(R.string.ms));
            wakeWaitTime.setMax(6000);
            wakeWaitTime.setOffset(1000);
            wakeWaitTime.setProgress(ZenDecision.getZenDecisionWakeWaitTime() / 1000);
            wakeWaitTime.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ZenDecision.setZenDecisionWakeWaitTime(position * 1000, getActivity());
                }
            });

            zenDecision.addItem(wakeWaitTime);
        }

        if (ZenDecision.hasZenDecisionBatThresholdIgnore()) {
            SeekBarView batThresholdIgnore = new SeekBarView();
            batThresholdIgnore.setTitle(getString(R.string.bat_threshold_ignore));
            batThresholdIgnore.setSummary(getString(R.string.bat_threshold_ignore_summary));
            batThresholdIgnore.setUnit("%");
            batThresholdIgnore.setMin(1);
            batThresholdIgnore.setProgress(ZenDecision.getZenDecisionBatThresholdIgnore());
            batThresholdIgnore.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    ZenDecision.setZenDecisionBatThresholdIgnore(position, getActivity());
                }
            });

            zenDecision.addItem(batThresholdIgnore);
        }

        if (zenDecision.size() > 0) {
            items.add(zenDecision);
        }
    }

    private void autoSmpInit(List<RecyclerViewItem> items) {
        CardView autoSmp = new CardView(getActivity());
        autoSmp.setTitle(getString(R.string.autosmp));

        if (AutoSmp.hasAutoSmpEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.autosmp));
            enable.setSummary(getString(R.string.autosmp_summary));
            enable.setChecked(AutoSmp.isAutoSmpEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AutoSmp.enableAutoSmp(isChecked, getActivity());
                }
            });

            autoSmp.addItem(enable);
            mEnableViews.add(enable);
        }

        if (AutoSmp.hasAutoSmpCpufreqDown()) {
            SeekBarView cpuFreqDown = new SeekBarView();
            cpuFreqDown.setTitle(getString(R.string.downrate_limits));
            cpuFreqDown.setUnit("%");
            cpuFreqDown.setProgress(AutoSmp.getAutoSmpCpufreqDown());
            cpuFreqDown.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpCpufreqDown(position, getActivity());
                }
            });

            autoSmp.addItem(cpuFreqDown);
        }

        if (AutoSmp.hasAutoSmpCpufreqUp()) {
            SeekBarView cpuFreqUp = new SeekBarView();
            cpuFreqUp.setTitle(getString(R.string.uprate_limits));
            cpuFreqUp.setUnit("%");
            cpuFreqUp.setProgress(AutoSmp.getAutoSmpCpufreqUp());
            cpuFreqUp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpCpufreqUp(position, getActivity());
                }
            });

            autoSmp.addItem(cpuFreqUp);
        }

        if (AutoSmp.hasAutoSmpCycleDown()) {
            SeekBarView cycleDown = new SeekBarView();
            cycleDown.setTitle(getString(R.string.cycle_down));
            cycleDown.setSummary(getString(R.string.cycle_down_summary));
            cycleDown.setMax(CPUFreq.getCpuCount());
            cycleDown.setProgress(AutoSmp.getAutoSmpCycleDown());
            cycleDown.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpCycleDown(position, getActivity());
                }
            });

            autoSmp.addItem(cycleDown);
        }

        if (AutoSmp.hasAutoSmpCycleUp()) {
            SeekBarView cycleUp = new SeekBarView();
            cycleUp.setTitle(getString(R.string.cycle_up));
            cycleUp.setSummary(getString(R.string.cycle_up_summary));
            cycleUp.setMax(CPUFreq.getCpuCount());
            cycleUp.setProgress(AutoSmp.getAutoSmpCycleUp());
            cycleUp.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpCycleUp(position, getActivity());
                }
            });

            autoSmp.addItem(cycleUp);
        }

        if (AutoSmp.hasAutoSmpDelay()) {
            SeekBarView delay = new SeekBarView();
            delay.setTitle(getString(R.string.delay));
            delay.setSummary(getString(R.string.delay_summary));
            delay.setUnit(getString(R.string.ms));
            delay.setMax(500);
            delay.setProgress(AutoSmp.getAutoSmpDelay());
            delay.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpDelay(position, getActivity());
                }
            });

            autoSmp.addItem(delay);
        }

        if (AutoSmp.hasAutoSmpMaxCpus()) {
            SeekBarView maxCpus = new SeekBarView();
            maxCpus.setTitle(getString(R.string.max_cpu_online));
            maxCpus.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpus.setMax(CPUFreq.getCpuCount());
            maxCpus.setMin(1);
            maxCpus.setProgress(AutoSmp.getAutoSmpMaxCpus() - 1);
            maxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpMaxCpus(position + 1, getActivity());
                }
            });

            autoSmp.addItem(maxCpus);
        }

        if (AutoSmp.hasAutoSmpMinCpus()) {
            SeekBarView minCpus = new SeekBarView();
            minCpus.setTitle(getString(R.string.min_cpu_online));
            minCpus.setSummary(getString(R.string.min_cpu_online_summary));
            minCpus.setMax(CPUFreq.getCpuCount());
            minCpus.setMin(1);
            minCpus.setProgress(AutoSmp.getAutoSmpMinCpus() - 1);
            minCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AutoSmp.setAutoSmpMinCpus(position + 1, getActivity());
                }
            });

            autoSmp.addItem(minCpus);
        }

        if (AutoSmp.hasAutoSmpScroffSingleCore()) {
            SwitchView scroffSingleCore = new SwitchView();
            scroffSingleCore.setTitle(getString(R.string.screen_off_single_cpu));
            scroffSingleCore.setSummary(getString(R.string.screen_off_single_cpu_summary));
            scroffSingleCore.setChecked(AutoSmp.isAutoSmpScroffSingleCoreEnabled());
            scroffSingleCore.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AutoSmp.enableAutoSmpScroffSingleCoreActive(isChecked, getActivity());
                }
            });

            autoSmp.addItem(scroffSingleCore);
        }

        if (autoSmp.size() > 0) {
            items.add(autoSmp);
        }
    }

    private void coreCtlInit(List<RecyclerViewItem> items) {
        List<RecyclerViewItem> coreCtl = new ArrayList<>();
        TitleView title = new TitleView();
        title.setText(getString(CoreCtl.hasEnable() ? R.string.hcube : R.string.core_control));

        if (CoreCtl.hasMinCpus(CPUFreq.getBigCpu())) {
            SeekBarView minCpus = new SeekBarView();
            minCpus.setTitle(getString(R.string.min_cpus_big));
            minCpus.setSummary(getString(R.string.min_cpus_big_summary));
            minCpus.setMax(CPUFreq.getBigCpuRange().size());
            minCpus.setProgress(CoreCtl.getMinCpus(CPUFreq.getBigCpu()));
            minCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    CoreCtl.setMinCpus(position, CPUFreq.getBigCpu(), getActivity());
                }

                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }
            });

            coreCtl.add(minCpus);
        }

        if (CoreCtl.hasBusyDownThreshold()) {
            SeekBarView busyDownThreshold = new SeekBarView();
            busyDownThreshold.setTitle(getString(R.string.busy_down_threshold));
            busyDownThreshold.setSummary(getString(R.string.busy_down_threshold_summary));
            busyDownThreshold.setProgress(CoreCtl.getBusyDownThreshold());
            busyDownThreshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    CoreCtl.setBusyDownThreshold(position, getActivity());
                }
            });

            coreCtl.add(busyDownThreshold);
        }

        if (CoreCtl.hasBusyUpThreshold()) {
            SeekBarView busyUpThreshold = new SeekBarView();
            busyUpThreshold.setTitle(getString(R.string.busy_up_threshold));
            busyUpThreshold.setSummary(getString(R.string.busy_up_threshold_summary));
            busyUpThreshold.setProgress(CoreCtl.getBusyUpThreshold());
            busyUpThreshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    CoreCtl.setBusyUpThreshold(position, getActivity());
                }
            });

            coreCtl.add(busyUpThreshold);
        }

        if (CoreCtl.hasOfflineDelayMs()) {
            SeekBarView offlineDelayMs = new SeekBarView();
            offlineDelayMs.setTitle(getString(R.string.offline_delay));
            offlineDelayMs.setSummary(getString(R.string.offline_delay_summary));
            offlineDelayMs.setUnit(getString(R.string.ms));
            offlineDelayMs.setMax(5000);
            offlineDelayMs.setOffset(100);
            offlineDelayMs.setProgress(CoreCtl.getOfflineDelayMs() / 100);
            offlineDelayMs.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    CoreCtl.setOfflineDelayMs(position * 100, getActivity());
                }
            });

            coreCtl.add(offlineDelayMs);
        }

        if (CoreCtl.hasOnlineDelayMs()) {
            SeekBarView onlineDelayMs = new SeekBarView();
            onlineDelayMs.setTitle(getString(R.string.online_delay));
            onlineDelayMs.setSummary(getString(R.string.online_delay_summary));
            onlineDelayMs.setUnit(getString(R.string.ms));
            onlineDelayMs.setMax(5000);
            onlineDelayMs.setOffset(100);
            onlineDelayMs.setProgress(CoreCtl.getOnlineDelayMs() / 100);
            onlineDelayMs.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    CoreCtl.setOnlineDelayMs(position * 100, getActivity());
                }
            });

            coreCtl.add(onlineDelayMs);
        }

        if (coreCtl.size() > 0) {
            items.add(title);

            if (CoreCtl.hasEnable()) {
                SwitchView enable = new SwitchView();
                enable.setTitle(getString(R.string.hcube));
                enable.setSummary(getString(R.string.hcube_summary));
                enable.setChecked(CoreCtl.isEnabled());
                enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                    @Override
                    public void onChanged(SwitchView switchView, boolean isChecked) {
                        CoreCtl.enable(isChecked, getActivity());
                    }
                });

                items.add(enable);
                mEnableViews.add(enable);
            } else {
                DescriptionView description = new DescriptionView();
                description.setTitle(getString(R.string.core_control));
                description.setSummary(getString(R.string.core_control_summary));
                items.add(description);
            }

            items.addAll(coreCtl);
        }
    }

    private void aioHotplugInit(List<RecyclerViewItem> items) {
        List<RecyclerViewItem> aioHotplug = new ArrayList<>();
        TitleView title = new TitleView();
        title.setText(getString(R.string.aio_hotplug));

        if (AiOHotplug.hasToggle()) {
            SwitchView toggle = new SwitchView();
            toggle.setTitle(getString(R.string.aio_hotplug));
            toggle.setSummary(getString(R.string.aio_hotplug_summary));
            toggle.setChecked(AiOHotplug.isEnabled());
            toggle.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AiOHotplug.enable(isChecked, getActivity());
                }
            });

            aioHotplug.add(toggle);
            mEnableViews.add(toggle);
        }

        if (AiOHotplug.hasCores()) {
            SeekBarView maxCpus = new SeekBarView();
            maxCpus.setTitle(getString(R.string.max_cpu_online));
            maxCpus.setSummary(getString(R.string.max_cpu_online_summary));
            maxCpus.setMax(CPUFreq.getCpuCount());
            maxCpus.setMin(1);
            maxCpus.setProgress(AiOHotplug.getCores() - 1);
            maxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AiOHotplug.setCores(position + 1, getActivity());
                }
            });

            aioHotplug.add(maxCpus);
        }

        if (CPUFreq.isBigLITTLE() && AiOHotplug.hasBigCores()) {
            List<String> list = new ArrayList<>();
            list.add("Disable");
            for (int i = 1; i <= CPUFreq.getBigCpuRange().size(); i++) {
                list.add(String.valueOf(i));
            }

            SeekBarView bigMaxCpus = new SeekBarView();
            bigMaxCpus.setTitle(getString(R.string.max_cpu_online_big));
            bigMaxCpus.setSummary(getString(R.string.max_cpu_online_big_summary));
            bigMaxCpus.setItems(list);
            bigMaxCpus.setProgress(AiOHotplug.getBigCores());
            bigMaxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AiOHotplug.setBigCores(position, getActivity());
                }
            });

            aioHotplug.add(bigMaxCpus);
        }

        if (CPUFreq.isBigLITTLE() && AiOHotplug.hasLITTLECores()) {
            List<String> list = new ArrayList<>();
            list.add("Disable");
            for (int i = 1; i <= CPUFreq.getLITTLECpuRange().size(); i++) {
                list.add(String.valueOf(i));
            }

            SeekBarView LITTLEMaxCpus = new SeekBarView();
            LITTLEMaxCpus.setTitle(getString(R.string.max_cpu_online_little));
            LITTLEMaxCpus.setSummary(getString(R.string.max_cpu_online_little_summary));
            LITTLEMaxCpus.setItems(list);
            LITTLEMaxCpus.setProgress(AiOHotplug.getLITTLECores());
            LITTLEMaxCpus.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AiOHotplug.setLITTLECores(position, getActivity());
                }
            });

            aioHotplug.add(LITTLEMaxCpus);
        }

        if (aioHotplug.size() > 0) {
            items.add(title);
            items.addAll(aioHotplug);
        }
    }

}
