package fransis.org.ar.radiustool.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import fransis.org.ar.radiustool.model.TestCase;

/**
 * Created by francisco on 10/6/16.
 */

public class TestCaseAdapter extends ArrayAdapter<TestCase> {

    public TestCaseAdapter(Context context, int resource, List<TestCase> list) {
        super(context, resource);
        for (TestCase tc :
                list) {
            this.add(tc);
        }
    }

}
