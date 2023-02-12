public class UserGeneration {

    public static User getDefault(){
        return new User("anna1@mail.ru", "1703", "Anna");
    }

    public static User getIncompleteDate(){
        return new User("anna@mail.ru", null, "Anna");
    }
    public static User getIncorrectDate(){
        return new User("anna@mail.ru", "3071", "Anna");
    }
    public static User getUpdateDate(){
        return new User("anna_1@mail.ru", "1703_1", "Anna_1");
    }
}
