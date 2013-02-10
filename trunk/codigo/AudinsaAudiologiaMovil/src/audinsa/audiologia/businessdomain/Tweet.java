package audinsa.audiologia.businessdomain;

public class Tweet {
	public String username;
	public String message;
	public String image_url;

	public Tweet(String username, String message, String url) {
		this.username = username;
		this.message = message;
		this.image_url = url;
	}
	
	public Tweet() {
		this.username = "";
		this.message = "";
		this.image_url = "";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
}
