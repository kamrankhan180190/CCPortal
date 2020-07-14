
package com.example.kamrankhan.pstapplication.PST;

public class PSTException extends Exception
{

	private static final long serialVersionUID = 4284698344354718143L;
	
	PSTException(String error) {
		super(error);
	}
	PSTException(String error, Exception orig) {
		super(error, orig);
	}
}
