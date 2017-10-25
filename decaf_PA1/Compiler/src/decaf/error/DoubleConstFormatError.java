package decaf.error;

import decaf.Location;

public class DoubleConstFormatError extends DecafError
{
	private String val;
	
	public DoubleConstFormatError(Location location, String val)
	{
		super(location);
		// TODO Auto-generated constructor stub
		this.val = val;
	}

	@Override
	protected String getErrMsg()
	{
		// TODO Auto-generated method stub
		return "string literal " + val + " is in wrong format";
	}

}
