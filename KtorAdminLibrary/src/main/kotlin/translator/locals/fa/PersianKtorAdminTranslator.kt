package translator.locals.fa

import translator.KtorAdminTranslator

object PersianKtorAdminTranslator : KtorAdminTranslator() {
    override val languageCode: String
        get() = "fa"

    override val languageName: String
        get() = "Persian"

    override val translates: Map<String, String> = mapOf(
        Keys.DASHBOARD to "داشبورد",
        Keys.ITEMS to "{count} مورد",
        Keys.CHANGE_THEME to "تغییر تم",
        Keys.DOWNLOAD_AS_CSV to "دانلود به عنوان CSV",
        Keys.DOWNLOAD_AS_PDF to "دانلود به عنوان PDF",
        Keys.PERFORM to "اجرا",
        Keys.DELETE_SELECTED_ITEMS to "حذف موارد انتخاب‌شده",
        Keys.ADD_NEW_ITEM to "افزودن {name} جدید",
        Keys.UPDATE_ITEM to "به‌روزرسانی {name}",
        Keys.RESET_ITEM to "بازنشانی {name}",
        Keys.SUBMIT to "ارسال",
        Keys.LINK to "لینک",
        Keys.SELECTED_FILE to "فایل انتخاب‌شده: {name}",
        Keys.SELECT_A_FILE to "انتخاب فایل",
        Keys.CURRENT_FILE to "فایل فعلی",
        Keys.EMPTY to "خالی",
        Keys.SEARCH to "جستجو...",
        Keys.LOGIN to "ورود",
        Keys.LOGOUT to "خروج",
        Keys.LANGUAGES to "زبان",
        Keys.FILTER to "فیلتر",

        Keys.SELECT_RANGE_OF to "محدوده‌ای از {value} را انتخاب کنید",
        Keys.SELECT_AN_ITEM_FOR to "یک مورد برای {value} انتخاب کنید",
        Keys.SELECT_AN_ITEM to "یک مورد انتخاب کنید",
        Keys.SELECT_A_BOOLEAN to "یک مقدار بولین برای {value} انتخاب کنید",
        Keys.SELECT_AN_ACTION to "یک اقدام انتخاب کنید",

        Keys.ERROR_NULL_FIELD to "این فیلد نمی‌تواند مقدار null داشته باشد",
        Keys.ERROR_EMPTY_FIELD to "این فیلد نمی‌تواند خالی باشد",
        Keys.ERROR_EMPTY_OR_NULL_REFERENCE to "در صورت نیاز به مرجع، این فیلد نمی‌تواند خالی یا null باشد.",
        Keys.ERROR_INVALID_VALUE to "مقدار وارد شده معتبر نیست.",
        Keys.ERROR_UNIQUE_FIELD to "این فیلد باید یکتا باشد",
        Keys.ERROR_MAX_LENGTH_EXCEEDED to "حداکثر طول مجاز {length} است",
        Keys.ERROR_MIN_LENGTH_NOT_MET to "حداقل طول مجاز {length} است",
        Keys.ERROR_REGEX_MISMATCH to "الگوی معتبر: {pattern}",
        Keys.ERROR_INVALID_INTEGER to "عدد صحیح معتبر وارد کنید",
        Keys.ERROR_INTEGER_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_INTEGER_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_UNSIGNED_INTEGER to "عدد صحیح نامنفی معتبر وارد کنید",
        Keys.ERROR_UNSIGNED_INTEGER_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_UNSIGNED_INTEGER_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_SHORT to "عدد کوتاه معتبر وارد کنید",
        Keys.ERROR_SHORT_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_SHORT_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_UNSIGNED_SHORT to "عدد کوتاه نامنفی معتبر وارد کنید",
        Keys.ERROR_UNSIGNED_SHORT_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_UNSIGNED_SHORT_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_LONG to "عدد بلند معتبر وارد کنید",
        Keys.ERROR_LONG_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_LONG_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_UNSIGNED_LONG to "عدد بلند نامنفی معتبر وارد کنید",
        Keys.ERROR_UNSIGNED_LONG_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_UNSIGNED_LONG_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_DOUBLE to "عدد اعشاری معتبر وارد کنید",
        Keys.ERROR_DOUBLE_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_DOUBLE_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_FLOAT to "عدد اعشاری معتبر وارد کنید",
        Keys.ERROR_FLOAT_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_FLOAT_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_INVALID_BIG_DECIMAL to "عدد اعشاری بزرگ معتبر وارد کنید",

        Keys.ERROR_INVALID_CHAR to "کاراکترهای نامعتبر وارد شده است",
        Keys.ERROR_INVALID_BOOLEAN to "مقدار باید true یا false باشد",
        Keys.ERROR_INVALID_ENUMERATION to "مقدار باید یکی از گزینه‌های {values} باشد",
        Keys.ERROR_INVALID_DATE to "تاریخ معتبر وارد کنید",
        Keys.ERROR_DATE_BEFORE_MIN to "تاریخ نباید قبل از {date} باشد",
        Keys.ERROR_DATE_AFTER_MAX to "تاریخ نباید بعد از {date} باشد",
        Keys.ERROR_INVALID_DURATION to "مدت‌زمان معتبر وارد کنید",
        Keys.ERROR_INVALID_DATETIME to "تاریخ و زمان معتبر وارد کنید ({format})",
        Keys.ERROR_DATETIME_BEFORE_MIN to "تاریخ و زمان نباید قبل از {datetime} باشد",
        Keys.ERROR_DATETIME_AFTER_MAX to "تاریخ و زمان نباید بعد از {datetime} باشد",
        Keys.ERROR_FILE_SIZE_EXCEEDED to "حجم فایل نباید بیشتر از {size} باشد",
        Keys.ERROR_INVALID_MIME_TYPE to "نوع فایل {file} نامعتبر است. نوع‌های مجاز: {types}",
        Keys.ERROR_INVALID_DECIMAL128 to "عدد Decimal128 معتبر وارد کنید",
        Keys.ERROR_DECIMAL128_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_DECIMAL128_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است",
        Keys.ERROR_BIG_DECIMAL_MAX_EXCEEDED to "حداکثر مقدار مجاز {max} است",
        Keys.ERROR_BIG_DECIMAL_MIN_EXCEEDED to "حداقل مقدار مجاز {min} است"
    )
}