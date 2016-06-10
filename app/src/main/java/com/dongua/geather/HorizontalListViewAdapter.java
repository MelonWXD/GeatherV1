package com.dongua.geather;


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.drawable.Drawable;
        import android.media.ThumbnailUtils;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{
    private int[] mIconIDs;
    private String[] mTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    Bitmap iconBitmap;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context, String[] titles, int[] ids){
        this.mContext = context;
        this.mIconIDs = ids;
        this.mTitles = titles;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mIconIDs.length;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){

        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(position == selectIndex){
            convertView.setSelected(true);
        }else{
            convertView.setSelected(false);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle ;
        private ImageView mImage;
    }
    private void getPropThumnail(int id){

    }
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}