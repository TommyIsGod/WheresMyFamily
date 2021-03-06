package dk.projekt.bachelor.wheresmyfamily.Controller;

import android.content.Context;

import java.util.ArrayList;

import dk.projekt.bachelor.wheresmyfamily.DataModel.Parent;
import dk.projekt.bachelor.wheresmyfamily.Storage.UserInfoStorage;

/**
 * Created by Tommy on 03-12-2014.
 */
public class ParentModelController {

    ArrayList<Parent> myParents = new ArrayList<Parent>();
    UserInfoStorage userInfoStorage = new UserInfoStorage();
    int parentListCount;

    public ParentModelController() {}

    public Parent getCurrentParent()
    {
        Parent temp = new Parent();


        if(myParents.size() > 0)
        {
            for(int i = 0; i < myParents.size(); i++)
            {
                if(myParents.get(i).getIsCurrent())
                    temp = myParents.get(i);
            }
        }

        if(temp != null)
            return temp;
        else
        {
            temp.setName("Unknown");
            return temp;
        }
    }

    public ArrayList<Parent> getMyParents(Context context)
    {
        myParents = userInfoStorage.loadParents(context);

        return myParents;
    }

    public void setMyParents(Context context, ArrayList<Parent> _myParents)
    {
        userInfoStorage.saveParents(context, _myParents);
    }
}
