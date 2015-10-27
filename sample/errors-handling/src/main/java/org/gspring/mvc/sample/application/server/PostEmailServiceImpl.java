package org.gspring.mvc.sample.application.server;

import org.gspring.mvc.sample.application.client.PostEmailService;
import org.gspring.mvc.sample.application.shared.ApplicationException;
import org.gspring.mvc.sample.application.shared.TooManyPostsException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("greetingService")
public class PostEmailServiceImpl implements PostEmailService {
	@Resource
	JdbcTemplate jdbcTemplate;

	private final int total = 10;
	private int count = 0;

	@Override
	public List<String> post(String email) throws ApplicationException {
		if(count ++ > total) {
			throw new TooManyPostsException();
		}

		String e = email.replace("'", "");
		// email is unique
		// lets intercept if it already exists
		jdbcTemplate.execute("insert into users(name, email) values('"+e+"', '"+e+"')");

		// return all
		return new ArrayList<String>(jdbcTemplate.query("select u.email from users as u", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		}));
	}
}
