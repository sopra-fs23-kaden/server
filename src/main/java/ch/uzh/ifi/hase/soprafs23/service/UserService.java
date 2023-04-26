package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);

    User userByUsername = userRepository.findByUsername(newUser.getUsername());
    User userByName = userRepository.findByName(newUser.getName());

      if (userByUsername != null){
        if (userByName == userByUsername){
            userByUsername.setStatus(UserStatus.ONLINE);
            log.debug("Login Successfully: {}", newUser);
            newUser = userByUsername;
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Wrong name!"));
        }
    }
    else{
        checkIfUserExists(newUser);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
    }
    return newUser;
  }

  public User logoutUser(String userName) {
      User updatedUser = userRepository.findByUsername(userName);
      updatedUser.setStatus(UserStatus.OFFLINE);
      updatedUser = userRepository.save(updatedUser);
      userRepository.flush();
      return updatedUser;
  }
  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
        String identity = "username: " + userByUsername.getUsername() + "/ name: " + userByName.getName() ;
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, identity, "are"));
    } else if (userByUsername != null) {
        String identity = "username: " + userByUsername.getUsername() ;
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, identity, "is"));
    } else if (userByName != null) {
        String identity = "name: " + userByName.getName() ;
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, identity, "is"));
    }
  }
}
