package decaf.error;

import decaf.Location;

public class BlockCommentError extends DecafError
{

	public BlockCommentError(Location location)
	{
		super(location);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getErrMsg()
	{
		// TODO Auto-generated method stub
		return "unclosed block comment";
	}

}
