# workexecutor
用HandlerThread和Handler组合代替AsyncTask功能

## 使用方法
WorkExecutor workExecutor = new WorkExecutor();
workExecutor.initDataProvider(new DataProvider<String>(){
    @Override
    public String getData() {
        return "hello";
    }
}).doBackground(new Executor<String, Integer>() {
    @Override
    public Integer execute(String data) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(getClass().getName(), "data length : " + data.length());
        return data.length();
    }
}).doUI(new Executor<Integer, Boolean>() {
    @Override
    public Boolean execute(Integer data) {
        Log.e(getClass().getName(), "finish data : " + data);
        return false;
    }
}).doUI(new Executor<Boolean, Void>() {
    @Override
    public Void execute(Boolean data) {
        Log.e(getClass().getName(), "finish data : " + data);
        return null;
    }
}).doException(new ExceptionWorker() {
    @Override
    public void workException(Exception exception) {
        Log.e(getClass().getName(), "exception : " + exception);
    }
}).execute();
