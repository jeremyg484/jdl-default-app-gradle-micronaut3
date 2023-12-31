package tech.jhipster.sample.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.cache.CacheManager;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.time.Instant;
import java.util.*;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import tech.jhipster.sample.domain.Authority;
import tech.jhipster.sample.domain.User;
import tech.jhipster.sample.repository.AuthorityRepository;
import tech.jhipster.sample.repository.UserRepository;
import tech.jhipster.sample.security.AuthoritiesConstants;
import tech.jhipster.sample.service.MailService;
import tech.jhipster.sample.service.dto.UserDTO;
import tech.jhipster.sample.service.mapper.UserMapper;
import tech.jhipster.sample.web.rest.vm.ManagedUserVM;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Inject
    private UserRepository userRepository;

    @Inject
    AuthorityRepository authorityRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject
    private UserMapper userMapper;

    @Inject
    private CacheManager cacheManager;

    @Inject
    @Client("/")
    Rx3HttpClient client;

    private User user;

    public static User createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Delete all User entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, User.class);
    }

    @BeforeAll
    public void initTest() {
        user = createEntity(transactionManager, em);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        userRepository.saveAndFlush(user);
        List<Authority> authorities = authorityRepository.findAll();
        if (authorities.isEmpty()) {
            // Set up expected authorities, ADMIN and USER
            Authority admin = new Authority();
            admin.setName(AuthoritiesConstants.ADMIN);
            authorityRepository.save(admin);
            Authority user = new Authority();
            user.setName(AuthoritiesConstants.USER);
            authorityRepository.save(user);
        }
    }

    @AfterAll
    public void cleanupTest() {
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        cacheManager.getCache(UserRepository.USERS_CACHE).invalidateAll();
    }

    private void resetDefaultUser() {
        User reInsert = createEntity(transactionManager, em);
        reInsert.setLogin(DEFAULT_LOGIN);
        reInsert.setEmail(DEFAULT_EMAIL);
        userRepository.deleteById(user.getId());
        userRepository.flush();
        user = userRepository.saveAndFlush(reInsert);
    }

    @Test
    public void createUser() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        // Create the User
        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin("createUser");
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail("createUser@localhost");
        managedUserVM.setActivated(true);
        managedUserVM.setImageUrl(DEFAULT_IMAGEURL);
        managedUserVM.setLangKey(DEFAULT_LANGKEY);
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        HttpResponse<User> response = client.exchange(HttpRequest.POST("/api/users", managedUserVM), User.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeCreate + 1);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getLogin()).isEqualTo("createuser");
        assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo("createuser@localhost");
        assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
    }

    @Test
    public void createUserWithExistingId() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(1L);
        managedUserVM.setLogin(DEFAULT_LOGIN);
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail(DEFAULT_EMAIL);
        managedUserVM.setActivated(true);
        managedUserVM.setImageUrl(DEFAULT_IMAGEURL);
        managedUserVM.setLangKey(DEFAULT_LANGKEY);
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // An entity with an existing ID cannot be created, so this API call must fail
        HttpResponse<User> response = client
            .exchange(HttpRequest.POST("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    public void createUserWithExistingLogin() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin(DEFAULT_LOGIN); // this login should already be used
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail("anothermail@localhost");
        managedUserVM.setActivated(true);
        managedUserVM.setImageUrl(DEFAULT_IMAGEURL);
        managedUserVM.setLangKey(DEFAULT_LANGKEY);
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        HttpResponse<User> response = client
            .exchange(HttpRequest.POST("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    public void createUserWithExistingEmail() throws Exception {
        long databaseSizeBeforeCreate = userRepository.count();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin("anotherlogin");
        managedUserVM.setPassword(DEFAULT_PASSWORD);
        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
        managedUserVM.setLastName(DEFAULT_LASTNAME);
        managedUserVM.setEmail(DEFAULT_EMAIL); // this email should already be used
        managedUserVM.setActivated(true);
        managedUserVM.setImageUrl(DEFAULT_IMAGEURL);
        managedUserVM.setLangKey(DEFAULT_LANGKEY);
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        HttpResponse<User> response = client
            .exchange(HttpRequest.POST("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllUsers() throws Exception {
        // Get all the users
        List<User> users = client.retrieve(HttpRequest.GET("/api/users?sort=id,desc"), Argument.listOf(User.class)).blockingFirst();

        assertThat(users.get(0).getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(users.get(0).getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(users.get(0).getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(users.get(0).getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(users.get(0).getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(users.get(0).getLangKey()).isEqualTo(DEFAULT_LANGKEY);
    }

    @Test
    public void getUser() throws Exception {
        assertThat(cacheManager.getCache(UserRepository.USERS_CACHE).get(user.getLogin(), User.class)).isNotPresent();

        // Get the user
        User u = client.retrieve(HttpRequest.GET("/api/users/" + user.getLogin()), User.class).blockingFirst();

        assertThat(u.getLogin()).isEqualTo(user.getLogin());
        assertThat(u.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(u.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(u.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(u.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(u.getLangKey()).isEqualTo(DEFAULT_LANGKEY);

        assertThat(cacheManager.getCache(UserRepository.USERS_CACHE).get(user.getLogin(), User.class)).isPresent();
    }

    @Test
    public void getNonExistingUser() throws Exception {
        HttpResponse<User> response = client
            .exchange(HttpRequest.GET("/api/users/unknown"), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateUser() throws Exception {
        long databaseSizeBeforeUpdate = userRepository.count();
        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin(updatedUser.getLogin());
        managedUserVM.setPassword(UPDATED_PASSWORD);
        managedUserVM.setFirstName(UPDATED_FIRSTNAME);
        managedUserVM.setLastName(UPDATED_LASTNAME);
        managedUserVM.setEmail(UPDATED_EMAIL);
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setImageUrl(UPDATED_IMAGEURL);
        managedUserVM.setLangKey(UPDATED_LANGKEY);
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        HttpResponse<User> response = client
            .exchange(HttpRequest.PUT("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);

        resetDefaultUser();
    }

    @Test
    public void updateUserLogin() throws Exception {
        long databaseSizeBeforeUpdate = userRepository.count();

        // Update the user
        User oldUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(oldUser.getId());
        managedUserVM.setLogin(UPDATED_LOGIN);
        managedUserVM.setPassword(UPDATED_PASSWORD);
        managedUserVM.setFirstName(UPDATED_FIRSTNAME);
        managedUserVM.setLastName(UPDATED_LASTNAME);
        managedUserVM.setEmail(UPDATED_EMAIL);
        managedUserVM.setActivated(oldUser.getActivated());
        managedUserVM.setImageUrl(UPDATED_IMAGEURL);
        managedUserVM.setLangKey(UPDATED_LANGKEY);
        managedUserVM.setCreatedDate(oldUser.getCreatedDate());
        managedUserVM.setLastModifiedDate(oldUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        HttpResponse<User> response = client
            .exchange(HttpRequest.PUT("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the User in the database
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeUpdate);
        User testUser = userList.stream().filter(u -> u.getId().equals(oldUser.getId())).findFirst().get();
        assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);

        resetDefaultUser();
    }

    @Test
    public void updateUserExistingEmail() throws Exception {
        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        anotherUser = userRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin(updatedUser.getLogin());
        managedUserVM.setPassword(updatedUser.getPassword());
        managedUserVM.setFirstName(updatedUser.getFirstName());
        managedUserVM.setLastName(updatedUser.getLastName());
        managedUserVM.setEmail("jhipster@localhost"); // this email should already be used by anotherUser
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setImageUrl(updatedUser.getImageUrl());
        managedUserVM.setLangKey(updatedUser.getLangKey());
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        HttpResponse<User> response = client
            .exchange(HttpRequest.PUT("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        userRepository.deleteById(anotherUser.getId());
        userRepository.flush();
    }

    @Test
    public void updateUserExistingLogin() throws Exception {
        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        ManagedUserVM managedUserVM = new ManagedUserVM();
        managedUserVM.setId(updatedUser.getId());
        managedUserVM.setLogin("jhipster"); // this login should already be used by anotherUser
        managedUserVM.setPassword(updatedUser.getPassword());
        managedUserVM.setFirstName(updatedUser.getFirstName());
        managedUserVM.setLastName(updatedUser.getLastName());
        managedUserVM.setEmail(updatedUser.getEmail());
        managedUserVM.setActivated(updatedUser.getActivated());
        managedUserVM.setImageUrl(updatedUser.getImageUrl());
        managedUserVM.setLangKey(updatedUser.getLangKey());
        managedUserVM.setCreatedDate(updatedUser.getCreatedDate());
        managedUserVM.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        HttpResponse<User> response = client
            .exchange(HttpRequest.PUT("/api/users", managedUserVM), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        userRepository.deleteById(anotherUser.getId());
        userRepository.flush();
    }

    @Test
    public void deleteUser() throws Exception {
        long databaseSizeBeforeDelete = userRepository.count();

        // Delete the user

        HttpResponse<User> response = client
            .exchange(HttpRequest.DELETE("/api/users/" + user.getLogin()), User.class)
            .onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

        assertThat(cacheManager.getCache(UserRepository.USERS_CACHE).get(user.getLogin(), User.class)).isEmpty();

        // Validate the database is empty
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(databaseSizeBeforeDelete - 1);

        resetDefaultUser();
    }

    @Test
    public void getAllAuthorities() throws Exception {
        HttpResponse<List<String>> response = client
            .exchange(HttpRequest.GET("/api/users/authorities"), Argument.listOf(String.class))
            .onErrorReturn(t -> (HttpResponse<List<String>>) ((HttpClientResponseException) t).getResponse())
            .blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(response.body()).contains(AuthoritiesConstants.USER);
        assertThat(response.body()).contains(AuthoritiesConstants.ADMIN);
    }

    @Test
    public void testUserEquals() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        assertThat(user1).isEqualTo(user2);
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    public void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getActivated()).isEqualTo(true);
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        //assertThat(user.getCreatedDate()).isNotNull();
        //assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    public void testUserToUserDTO() {
        user.setId(DEFAULT_ID);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedDate(Instant.now());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.setAuthorities(authorities);

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isEqualTo(true);
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    public void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isEqualTo(authorityA);
        assertThat(authorityA).isNotEqualTo(null);
        assertThat(authorityA).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isEqualTo(0);
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB);
        assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
    }

    @MockBean(MailService.class)
    MailService mailService() {
        return Mockito.mock(MailService.class);
    }
}
