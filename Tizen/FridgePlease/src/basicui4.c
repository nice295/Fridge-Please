//#include "webviewexample.h"
//#include <refrigerator.h>
#include "basicui4.h"
#include <Evas.h>
#include <EWebKit.h>

typedef struct appdata {
	Evas_Object* win;
	Evas_Object* conform;
	Evas_Object* naviframe;
	Evas_Object* box;
	Evas_Object *webview_box;
	Evas_Object *webview;
	//refrigerator_h handle;
} appdata_s;


static void
create_base_gui(appdata_s *ad)
{
	if (!ewk_init()) {
		dlog_print(DLOG_ERROR, LOG_TAG, "create_base_gui() is failed");
		return;
	}

	/* Create Window */
	Evas_Object* window;
	Evas_Object* webview;
	window = elm_win_util_standard_add("Application", "Application");
	evas_object_show(window);


	/* Create Webview */
	webview = ewk_view_add(evas_object_evas_get(window));
	elm_win_resize_object_add(window, webview);
	evas_object_size_hint_weight_set(webview, EVAS_HINT_EXPAND, EVAS_HINT_EXPAND);
	evas_object_size_hint_align_set(webview, EVAS_HINT_FILL, EVAS_HINT_FILL);
	ewk_view_orientation_send(webview, -90);
	ewk_view_url_set(webview, "https://fridgeplease.firebaseapp.com/");
	//ewk_view_url_set(webview, "https://www.naver.com/");
	evas_object_show(webview);

}

static bool
app_create(void *data)
{
	/* Hook to take necessary actions before main event loop starts
	   Initialize UI resources and application's data
	   If this function returns true, the main loop of application starts
	   If this function returns false, the application is terminated */
	appdata_s *ad = data;
	create_base_gui(ad);

	return true;
}

static void
app_control(app_control_h app_control, void *data)
{
	/* Handle the launch request. */
}

static void
app_pause(void *data)
{
	/* Take necessary actions when application becomes invisible. */
}

static void
app_resume(void *data)
{
	/* Take necessary actions when application becomes visible. */
}

static void
app_terminate(void *data)
{
	/* Take necessary actions when application terminates. */
}

int
main(int argc, char *argv[])
{
	appdata_s ad = {0,};
	int ret = 0;

	ui_app_lifecycle_callback_s event_callback = {0,};

	event_callback.create = app_create;
	event_callback.terminate = app_terminate;
	event_callback.pause = app_pause;
	event_callback.resume = app_resume;
	event_callback.app_control = app_control;

	ret = ui_app_main(argc, argv, &event_callback, &ad);
	if (ret != APP_ERROR_NONE) {
		dlog_print(DLOG_ERROR, LOG_TAG, "ui_app_main() is failed. err = %d", ret);
	}

	return ret;
}
