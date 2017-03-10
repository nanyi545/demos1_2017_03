package test1.nh.com.demos1.mvpDemo.pack;

/**
 * Created by Administrator on 16-1-3.
 */
public class MVPpresenterDemo implements IDemoPresenter{


    private IDemoView mDemoView;
    private IDemoModel mDemoModel;

    public MVPpresenterDemo(IDemoView mDemoView,IDemoModel mDemoModel){
        this.mDemoView=mDemoView;
        this.mDemoModel=mDemoModel;
    }

    @Override
    public void loadInfo() {
        mDemoView.setLastName_view(mDemoModel.getLastName());
        mDemoView.setFirstName_view(mDemoModel.getFirstName());
        mDemoView.setId_view(mDemoModel.get_Id());
    }

    @Override
    public void saveINFO() {
        mDemoModel.saveFirstName(mDemoView.getFirstName_view());
        mDemoModel.saveLastName(mDemoView.getLastName_view());
        mDemoModel.saveId(mDemoView.getId_view());
    }
}
