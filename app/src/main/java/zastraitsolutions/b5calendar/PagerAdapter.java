package zastraitsolutions.b5calendar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import zastraitsolutions.b5calendar.Calendar.CalendarFragment;
import zastraitsolutions.b5calendar.EventsHIstory.EventHistoryFragment;

class PagerAdapter extends FragmentPagerAdapter {

    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CalendarFragment calendarFragment = new CalendarFragment();
                return calendarFragment;

            case 1:

                EventHistoryFragment eventHistoryFragment = new EventHistoryFragment();
                return eventHistoryFragment;

            default:

                return null;
        }
    }

    @Override
    public int getCount() {

        return numberOfTabs;
    }
}