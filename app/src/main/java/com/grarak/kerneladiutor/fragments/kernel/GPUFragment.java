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
import com.grarak.kerneladiutor.fragments.BaseFragment;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.kernel.gpu.AdrenoIdler;
import com.grarak.kerneladiutor.utils.kernel.gpu.GPUFreq;
import com.grarak.kerneladiutor.utils.kernel.gpu.SimpleGPU;
import com.grarak.kerneladiutor.utils.kernel.gpu.TieredGPU;
import com.grarak.kerneladiutor.views.recyclerview.CardView;
import com.grarak.kerneladiutor.views.recyclerview.DescriptionView;
import com.grarak.kerneladiutor.views.recyclerview.RecyclerViewItem;
import com.grarak.kerneladiutor.views.recyclerview.SeekBarView;
import com.grarak.kerneladiutor.views.recyclerview.SelectView;
import com.grarak.kerneladiutor.views.recyclerview.SwitchView;
import com.grarak.kerneladiutor.views.recyclerview.TitleView;
import com.grarak.kerneladiutor.views.recyclerview.XYGraphView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 12.05.16.
 */
public class GPUFragment extends RecyclerViewFragment {

    private XYGraphView m2dCurFreq;
    private XYGraphView mCurFreq;

    private PathReaderFragment mGPUGovernorTunableFragment;

    @Override
    protected BaseFragment getForegroundFragment() {
        return mGPUGovernorTunableFragment = new PathReaderFragment();
    }

    @Override
    protected void init() {
        super.init();

        addViewPagerFragment(ApplyOnBootFragment.newInstance(this));
    }

    @Override
    protected void addItems(List<RecyclerViewItem> items) {
        freqInit(items);
        governorInit(items);
        if (SimpleGPU.supported()) {
            simpleGpuInit(items);
        }
        if (TieredGPU.supported()) {
            tieredGpuInit(items);
        }
        if (AdrenoIdler.supported()) {
            adrenoIdlerInit(items);
        }
    }

    private void freqInit(List<RecyclerViewItem> items) {
        CardView freqCard = new CardView(getActivity());
        freqCard.setTitle(getString(R.string.frequencies));

        if (GPUFreq.has2dCurFreq() && GPUFreq.get2dAvailableFreqs() != null) {
            m2dCurFreq = new XYGraphView();
            m2dCurFreq.setTitle(getString(R.string.gpu_2d_freq));
            freqCard.addItem(m2dCurFreq);
        }

        if (GPUFreq.hasCurFreq() && GPUFreq.getAvailableFreqs() != null) {
            mCurFreq = new XYGraphView();
            mCurFreq.setTitle(getString(R.string.gpu_freq));
            freqCard.addItem(mCurFreq);
        }

        if (GPUFreq.has2dMaxFreq() && GPUFreq.get2dAvailableFreqs() != null) {
            SelectView max2dFreq = new SelectView();
            max2dFreq.setTitle(getString(R.string.gpu_2d_max_freq));
            max2dFreq.setSummary(getString(R.string.gpu_2d_max_freq_summary));
            max2dFreq.setItems(GPUFreq.get2dAdjustedFreqs(getActivity()));
            max2dFreq.setItem((GPUFreq.get2dMaxFreq() / 1000000) + getString(R.string.mhz));
            max2dFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    GPUFreq.set2dMaxFreq(GPUFreq.get2dAvailableFreqs().get(position), getActivity());
                }
            });

            freqCard.addItem(max2dFreq);
        }

        if (GPUFreq.hasMaxFreq() && GPUFreq.getAvailableFreqs() != null) {
            SelectView maxFreq = new SelectView();
            maxFreq.setTitle(getString(R.string.gpu_max_freq));
            maxFreq.setSummary(getString(R.string.gpu_max_freq_summary));
            maxFreq.setItems(GPUFreq.getAdjustedFreqs(getActivity()));
            maxFreq.setItem((GPUFreq.getMaxFreq() / GPUFreq.getMaxFreqOffset()) + getString(R.string.mhz));
            maxFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    GPUFreq.setMaxFreq(GPUFreq.getAvailableFreqs().get(position), getActivity());
                }
            });

            freqCard.addItem(maxFreq);
        }

        if (GPUFreq.hasMinFreq() && GPUFreq.getAvailableFreqs() != null) {
            SelectView minFreq = new SelectView();
            minFreq.setTitle(getString(R.string.gpu_min_freq));
            minFreq.setSummary(getString(R.string.gpu_min_freq_summary));
            minFreq.setItems(GPUFreq.getAdjustedFreqs(getActivity()));
            minFreq.setItem((GPUFreq.getMinFreq() / GPUFreq.getMinFreqOffset()) + getString(R.string.mhz));
            minFreq.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    GPUFreq.setMinFreq(GPUFreq.getAvailableFreqs().get(position), getActivity());
                }
            });

            freqCard.addItem(minFreq);
        }

        if (freqCard.size() > 0) {
            items.add(freqCard);
        }
    }

    private void governorInit(List<RecyclerViewItem> items) {
        if (GPUFreq.has2dGovernor()) {
            SelectView governor2d = new SelectView();
            governor2d.setTitle(getString(R.string.gpu_2d_governor));
            governor2d.setSummary(getString(R.string.gpu_2d_governor_summary));
            governor2d.setItems(GPUFreq.get2dAvailableGovernors());
            governor2d.setItem(GPUFreq.get2dGovernor());
            governor2d.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    GPUFreq.set2dGovernor(item, getActivity());
                }
            });

            items.add(governor2d);
        }

        if (GPUFreq.hasGovernor()) {
            SelectView governor = new SelectView();
            governor.setTitle(getString(R.string.gpu_governor));
            governor.setSummary(getString(R.string.gpu_governor_summary));
            governor.setItems(GPUFreq.getAvailableGovernors());
            governor.setItem(GPUFreq.getGovernor());
            governor.setOnItemSelected(new SelectView.OnItemSelected() {
                @Override
                public void onItemSelected(SelectView selectView, int position, String item) {
                    GPUFreq.setGovernor(item, getActivity());
                }
            });

            items.add(governor);

            if (GPUFreq.hasTunables(governor.getValue())) {
                DescriptionView tunables = new DescriptionView();
                tunables.setTitle(getString(R.string.gpu_governor_tunables));
                tunables.setSummary(getString(R.string.governor_tunables_summary));
                tunables.setOnItemClickListener(new RecyclerViewItem.OnItemClickListener() {
                    @Override
                    public void onClick(RecyclerViewItem item) {
                        String governor = GPUFreq.getGovernor();
                        setForegroundText(governor);
                        mGPUGovernorTunableFragment.setError(getString(R.string.tunables_error, governor));
                        mGPUGovernorTunableFragment.setPath(GPUFreq.getTunables(GPUFreq.getGovernor()),
                                ApplyOnBootFragment.GPU);
                        showForeground();
                    }
                });

                items.add(tunables);
            }
        }
    }

    private void simpleGpuInit(List<RecyclerViewItem> items) {
        CardView simpleGpu = new CardView(getActivity());
        simpleGpu.setTitle(getString(R.string.simple_gpu_algorithm));

        if (SimpleGPU.hasSimpleGpuEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.simple_gpu_algorithm));
            enable.setSummary(getString(R.string.simple_gpu_algorithm_summary));
            enable.setChecked(SimpleGPU.isSimpleGpuEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    SimpleGPU.enableSimpleGpu(isChecked, getActivity());
                }
            });

            simpleGpu.addItem(enable);
        }

        if (SimpleGPU.hasSimpleGpuLaziness()) {
            SeekBarView laziness = new SeekBarView();
            laziness.setTitle(getString(R.string.laziness));
            laziness.setSummary(getString(R.string.laziness_summary));
            laziness.setMax(10);
            laziness.setProgress(SimpleGPU.getSimpleGpuLaziness());
            laziness.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SimpleGPU.setSimpleGpuLaziness(position, getActivity());
                }
            });

            simpleGpu.addItem(laziness);
        }

        if (SimpleGPU.hasSimpleGpuRampThreshold()) {
            SeekBarView rampThreshold = new SeekBarView();
            rampThreshold.setTitle(getString(R.string.ramp_thresold));
            rampThreshold.setSummary(getString(R.string.ramp_thresold_summary));
            rampThreshold.setMax(10);
            rampThreshold.setProgress(SimpleGPU.getSimpleGpuRampThreshold());
            rampThreshold.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    SimpleGPU.setSimpleGpuRampThreshold(position, getActivity());
                }
            });

            simpleGpu.addItem(rampThreshold);
        }

        if (simpleGpu.size() > 0) {
            items.add(simpleGpu);
        }
    }

    private void tieredGpuInit(List<RecyclerViewItem> items) {
        CardView tieredGpu = new CardView(getActivity());
        tieredGpu.setTitle(getString(R.string.tiered_gpu_governor));

        if (TieredGPU.hasTieredGpuEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.tiered_gpu_governor));
            enable.setSummary(getString(R.string.tiered_gpu_governor_summary));
            enable.setChecked(TieredGPU.isTieredGpuEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    TieredGPU.enableTieredGpu(isChecked, getActivity());
                }
            });

            tieredGpu.addItem(enable);
        }

        if (TieredGPU.hasTieredEfficiencyModeEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.tiered_efficiency_mode));
            enable.setSummary(getString(R.string.tiered_efficiency_mode_summary));
            enable.setChecked(TieredGPU.isTieredEfficiencyModeEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    TieredGPU.enableTieredEfficiencyMode(isChecked, getActivity());
                }
            });

            tieredGpu.addItem(enable);
        }

        if (TieredGPU.hasTieredGpuSampleTime()) {
            SeekBarView sampleTime = new SeekBarView();
            sampleTime.setTitle(getString(R.string.tiered_sample_time));
            sampleTime.setSummary(getString(R.string.tiered_sample_time_summary));
            sampleTime.setMax(100);
            sampleTime.setProgress(TieredGPU.getTieredGpuSampleTime());
            sampleTime.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    TieredGPU.setTieredGpuSampleTime(position, getActivity());
                }
            });

            tieredGpu.addItem(sampleTime);
        }

        if (tieredGpu.size() > 0) {
            items.add(tieredGpu);
        }
    }

    private void adrenoIdlerInit(List<RecyclerViewItem> items) {
        CardView adrenoIdler = new CardView(getActivity());
        adrenoIdler.setTitle(getString(R.string.adreno_idler));

        if (AdrenoIdler.hasAdrenoIdlerEnable()) {
            SwitchView enable = new SwitchView();
            enable.setTitle(getString(R.string.adreno_idler));
            enable.setSummary(getString(R.string.adreno_idler_summary));
            enable.setChecked(AdrenoIdler.isAdrenoIdlerEnabled());
            enable.addOnSwitchListener(new SwitchView.OnSwitchListener() {
                @Override
                public void onChanged(SwitchView switchView, boolean isChecked) {
                    AdrenoIdler.enableAdrenoIdler(isChecked, getActivity());
                }
            });

            adrenoIdler.addItem(enable);
        }

        if (AdrenoIdler.hasAdrenoIdlerDownDiff()) {
            SeekBarView downDiff = new SeekBarView();
            downDiff.setTitle(getString(R.string.down_differential));
            downDiff.setSummary(getString(R.string.down_differential_summary));
            downDiff.setMax(99);
            downDiff.setProgress(AdrenoIdler.getAdrenoIdlerDownDiff());
            downDiff.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AdrenoIdler.setAdrenoIdlerDownDiff(position, getActivity());
                }
            });

            adrenoIdler.addItem(downDiff);
        }

        if (AdrenoIdler.hasAdrenoIdlerIdleWait()) {
            SeekBarView idleWait = new SeekBarView();
            idleWait.setTitle(getString(R.string.idle_wait));
            idleWait.setSummary(getString(R.string.idle_wait_summary));
            idleWait.setMax(99);
            idleWait.setProgress(AdrenoIdler.getAdrenoIdlerIdleWait());
            idleWait.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AdrenoIdler.setAdrenoIdlerIdleWait(position, getActivity());
                }
            });

            adrenoIdler.addItem(idleWait);
        }

        if (AdrenoIdler.hasAdrenoIdlerIdleWorkload()) {
            SeekBarView idleWorkload = new SeekBarView();
            idleWorkload.setTitle(getString(R.string.workload));
            idleWorkload.setSummary(getString(R.string.workload_summary));
            idleWorkload.setMax(10);
            idleWorkload.setMin(1);
            idleWorkload.setProgress(AdrenoIdler.getAdrenoIdlerIdleWorkload() - 1);
            idleWorkload.setOnSeekBarListener(new SeekBarView.OnSeekBarListener() {
                @Override
                public void onMove(SeekBarView seekBarView, int position, String value) {
                }

                @Override
                public void onStop(SeekBarView seekBarView, int position, String value) {
                    AdrenoIdler.setAdrenoIdlerIdleWorkload(position + 1, getActivity());
                }
            });

            adrenoIdler.addItem(idleWorkload);
        }

        if (adrenoIdler.size() > 0) {
            items.add(adrenoIdler);
        }
    }

    @Override
    protected void refresh() {
        super.refresh();

        if (m2dCurFreq != null) {
            int freq = GPUFreq.get2dCurFreq();
            float maxFreq = GPUFreq.get2dAvailableFreqs().get(GPUFreq.get2dAvailableFreqs().size() - 1);
            m2dCurFreq.setText((freq / 1000000) + getString(R.string.mhz));
            float per = (float) freq / maxFreq * 100f;
            m2dCurFreq.addPercentage(Math.round(per > 100 ? 100 : per < 0 ? 0 : per));
        }

        if (mCurFreq != null) {
            int load = -1;
            String text = "";
            if (GPUFreq.hasBusy()) {
                load = GPUFreq.getBusy();
                load = load > 100 ? 100 : load < 0 ? 0 : load;
                text += load + "% - ";
            }

            int freq = GPUFreq.getCurFreq();
            float maxFreq = GPUFreq.getAvailableFreqs().get(GPUFreq.getAvailableFreqs().size() - 1);
            text += freq / GPUFreq.getCurFreqOffset() + getString(R.string.mhz);
            mCurFreq.setText(text);
            float per = (float) freq / maxFreq * 100f;
            mCurFreq.addPercentage(load >= 0 ? load : Math.round(per > 100 ? 100 : per < 0 ? 0 : per));
        }
    }
}
