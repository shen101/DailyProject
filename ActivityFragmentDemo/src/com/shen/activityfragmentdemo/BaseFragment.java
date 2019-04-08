package com.shen.activityfragmentdemo;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.animation.Animation;

@SuppressLint("CommitTransaction")
public class BaseFragment extends Fragment {

	private FragmentManager mManager = null;
	private FragmentTransaction mTransaction = null;

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		Log.i("shen", "transit = " + transit + ",  enter = " + enter + ",   nextAnim = " + nextAnim);
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	public FragmentTransaction getRightTransaction(Fragment mFragment) {
		mManager = getFragmentManager();
		mTransaction = mManager.beginTransaction();
		mTransaction.setCustomAnimations(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
				R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
		mTransaction.add(R.id.id_content, mFragment);
		mTransaction.addToBackStack(null);
		mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
		mTransaction.commit();
		return mTransaction;
	}

	public FragmentTransaction getLeftTransaction(Fragment mFragment) {
		mManager = getFragmentManager();
		mTransaction = mManager.beginTransaction();
		mTransaction.setCustomAnimations(R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out,
				R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out);
		mTransaction.add(R.id.id_content, mFragment);
		mTransaction.addToBackStack(null);
		mTransaction.commit();
		return mTransaction;
	}

}
