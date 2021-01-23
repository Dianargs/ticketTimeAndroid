package com.example.movie_app;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;*/
    Toolbar mToolbar;
    MaterialSearchView mMaterialSearchView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> moviesList;



    private static String[] cities = new String[]{
            "Aveiro",
            "Beja",
            "Braga",
            "Bragança",
            "Castelo Branco",
            "Coimbra",
            "Évora",
            "Faro",
            "Guarda",
            "Leiria",
            "Lisboa",
            "Portalegre",
            "Porto",
            "Santarém",
            "Setúbal",
            "Viana do Castelo",
            "Vila Real",
            "Viseu"
    };

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
  /*  public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

         mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#FFFFFE"));

        mMaterialSearchView = view.findViewById(R.id.searchView);
        mMaterialSearchView.setSuggestions(cities);
        moviesList = new ArrayList<>();

        //para aparecer as sugestoes
        ListView listView = view.findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,cities);
        listView.setAdapter(arrayAdapter);

        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ListView listView = view.findViewById(R.id.listView);
                ArrayAdapter arrayAdapter =  new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,cities);
                listView.setAdapter(arrayAdapter);
            }
        });
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("MinhaTag","AQUIMF");
                //Log.d("MinhaTag",query);
                //it's case sensitive have to change in the firestore all lower or all upper
                //query.toLowerCaser();
                db.collection("cinemas_location").whereEqualTo("city",query)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                int i=0;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String mv = doc.getString("name");
                                        moviesList.add(mv);

                                        Log.d("MYTAG", "Nome" + " => " + mv);
                                    }
                                    ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,moviesList);
                                    listView.setAdapter(arrayAdapter);

                                } else {
                                    Log.w("MYTAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                //porque sem isto me mostra duas listas dont know why
                for (int i = 0; i < listView.getChildCount(); i++) {
                    ((TextView)listView.getChildAt(i)).setTextColor(getResources().getColor(R.color.transparente));
                }

                return true;
            }
        });


        setHasOptionsMenu(true);

        return view;
    }
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        mMaterialSearchView.setMenuItem(menuItem);

        super.onCreateOptionsMenu(menu,inflater);
    }



}