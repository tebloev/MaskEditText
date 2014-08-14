package com.example.maskedit;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MainActivity extends Activity {
	public static int mCursor;
	EditText mEditText;
	final boolean[] mBack = new boolean[1];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mEditText = (EditText) findViewById(R.id.editText1);
        
        mEditText.setOnKeyListener(new OnKeyListener() {						
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_DEL){                        
                      mBack[0] = true;                      
                     } 
				else
					  mBack[0] = false;                 
            return false;
			}
		});
		mEditText.addTextChangedListener(new SimplePhoneTextWatcher());
	}
	
	private class SimplePhoneTextWatcher implements TextWatcher
	{
		int mStatPlus, mStatMinus;
		String mModPhone = "";
		String current = "";
		@Override
		public void afterTextChanged(Editable s) {
			
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			if (s.length() > 19)
				s = s.subSequence(0, 19);
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {			
			if(!s.toString().equals(mModPhone))
			{		
				mModPhone = "";		
				String mEmpty = "           ";
				String mAreaCode, mPrefix, mSuffix;
				String clean = s.toString().replaceAll("[^\\d.]", ""); 
				String currentClean = current.replaceAll("[^\\d.]", ""); 				
				if (clean.length() > 11)
					clean = clean.substring(0, 11);
				int cl = clean.length();
				switch (clean.length())
				{
					case 1:
						mStatPlus = 3;
						break;
					case 4: 
						mStatPlus = 5;
						break;
					case 7: 
						mStatPlus = 7;
						break;
				}
				mCursor = cl + mStatPlus;
				if (cl == 1)
					mStatMinus = 0;

				if (clean.equals(currentClean))	
					{
						switch (clean.length())
						{
							case 1: 
								mStatMinus = 0;
								clean = clean.substring(0, clean.length()-1);
								break;
							case 5: 
								mStatMinus = -6;
								clean = clean.substring(0, clean.length()-1);
								break;
							case 7: 
								clean = clean.substring(0, clean.length()-1);
								mStatMinus = -3;							
								break;
						}	
					}
				else
					if (cl <= 5 && !mBack[0])
						mStatMinus = 0;
				
				mCursor += mStatMinus;
				if ((start + 1) <= s.length())
				{
					String mDel = s.toString().substring(start+1);
					int mDelCount = mDel.replaceAll("[^\\d.]", "").length(); 
					if (mDelCount > 0 && cl > 1)
					{
						mCursor = start + 1;
						if (cl > 5 && cl < 10 && mBack[0])					
							mCursor--;
					}
				}
//				if (mBack[0])
//					mCursor = start;
//				if (s.length() > 1)
//					clean = clean.substring(1, clean.length());
				if (clean.length() <= 11)
					clean = clean + mEmpty.substring(clean.length());
	//			switch(clean.length())
	//			{
	//			case 1:
	//				mModString = "+7(" + clean + "  )";
	//			case 3:
	//				mModString = "+7(" + clean + "  )";
	//			}
				if (cl == 1)
					{
						if (mBack[0])	
						{
							clean = mEmpty;
							mCursor = 3;
						}
						mAreaCode = clean.substring(0, 3);
					}
				else
					mAreaCode = clean.substring(1, 4);
				mPrefix = clean.substring(4, 7);
				mSuffix = clean.substring(7, 11);
				current = clean;
				mModPhone = "+7(" + mAreaCode + ") " + mPrefix + " - " + mSuffix;
	            mEditText.setText(mModPhone);
	            mEditText.setSelection(mCursor);
			}
		}
		
	}
	
	private class PhoneTextWatcher implements TextWatcher
	{
		private String current = "";
		private int cursor = 0;  
		private String num = "           ";
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (!s.toString().equals(current)) {
	            String clean = s.toString().replaceAll("[^\\d.]", "");
	            String cleanC = current.replaceAll("[^\\d.]", "");
	            if (clean.length() > 11)
	            	clean = clean.substring(0, 11);
	            int cl = clean.length();
	            int sel = cl;		            
	            if (s.length()>1)
	            for (int i = 0;  i <= cl + 3 && i < 12; i++) {
	                if(!Character.isDigit(s.charAt(i)))
	                	sel++;
	            }
	            if (clean.equals(cleanC)) 
	            {
	            	sel--;
	            	if (!Character.isDigit(s.charAt(sel-1)))
	            		sel = sel-2;		            	
	            }
	            
	            	
	            if (clean.length() <= 10){
	               clean = clean + num.substring(clean.length());
	            } 
	            String z = clean.substring(0, 3);
	            z = clean.substring(6, 10);
	            clean = String.format("+%s(%s)%s-%s", 
	            	clean.substring(0, 1),
	            	clean.substring(1, 4),
	                clean.substring(4, 7),
	                clean.substring(7, 11));
	            current = clean;
	            mEditText.setText(clean);
            	if(mBack[0])
            		sel = start;
	            if (s.length() != 1)
	            	mEditText.setSelection(sel);
	            else
	            	mEditText.setSelection(3);
	        }
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	}
}
