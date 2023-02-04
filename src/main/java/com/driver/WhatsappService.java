package com.driver;

import java.util.List;

public class WhatsappService {
    WhatsappRepository repo=new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"

        if(repo.getUserMobile().contains(mobile))
            throw new Exception("User already exists");

        User user=new User(name, mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        Group group=null;
        if(users.size()==2){
            group=new Group(users.get(1).getName(), users.size());
        }
        else {
            group=new Group("Group "+repo.getCustomGroupCount(), users.size());
        }

        repo.getGroupUserMap().put(group,users);
        repo.getAdminMap().put(group,users.get(0));

        return group;
    }

    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        // Return the message id.

        return repo.getMessageId();

    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.

        if(!repo.getAdminMap().containsKey(group))
            throw new Exception("Group does not exist");
        boolean f=false;
        for( User participant: repo.getGroupUserMap().get(group))
        {
            if(participant.equals(sender))
            {
                f=true;
                break;
            }
        }

        if(f==false)
            throw new Exception("You are not allowed to send message");

        repo.getGroupMessageMap().get(group).add(message);
        repo.getSenderMap().put(message,sender);

        return repo.getGroupMessageMap().get(group).size();
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        if(!repo.getGroupMessageMap().containsKey(group))
            throw new Exception("Group does not exist");

        if(!repo.getAdminMap().get(group).equals(approver))
            throw new Exception("Approver does not have rights");
        boolean f=false;

        for(User u: repo.getGroupUserMap().get(group))
        {
            if(u.equals(user)){
                f=true;
                break;
            }
        }
        if(f==false)
            throw new Exception("User is not a participant");

        repo.getAdminMap().put(group,user);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)

        return 0;

    }
}
