package spyra.lukasz.javaquizzes.userstatistics;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    List<UserView> toView(List<User> users) {
        return users.stream()
                .map(this::toView)
                .collect(Collectors.toList());
    }

    UserView toView(User user) {
        UserView view = new UserView();
        view.setId(user.getId());
        view.setName(user.getName());
        view.setEmail(user.getEmail());
        view.setPassword(user.getPassword());
        return view;
    }

}
