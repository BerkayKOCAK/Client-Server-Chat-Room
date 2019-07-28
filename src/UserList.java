import java.util.ArrayList;

public final class UserList {

	private static ArrayList<String> userList = new ArrayList<String>();
	public static final void setUserList(ArrayList<String> userList) {
		UserList.userList = userList;
	}
	public static final ArrayList<String> getUserList() {
		return userList;
	}
}
